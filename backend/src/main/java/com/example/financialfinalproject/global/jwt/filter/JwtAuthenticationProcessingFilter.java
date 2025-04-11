package com.example.financialfinalproject.global.jwt.filter;

import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.global.jwt.service.JwtService;
import com.example.financialfinalproject.global.jwt.util.PasswordUtil;
import com.example.financialfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.authority.mapping.NullAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

/**
 * Jwt 인증 필터
 * NO_CHECK_URL 이외의 URI 요청이 왔을 때 처리하는 필터
 * 기본적으로 사용자는 요청 헤더에 AccessToken만 담아서 요청
 * AccessToken 만료 시에만 RefreshToken을 요청 헤더에 AccessToken과 함께 요청
 * 1. RefreshToken이 없고, AccessToken이 유효한 경우 -> 인증 성공 처리, RefreshToken을 재발급하지는 않는다.
 * 2. RefreshToken이 없고, AccessToken이 없거나 유효하지 않은 경우 -> 인증 실패 처리
 * 3. RefreshToken이 있는 경우 -> Redis의 RefreshToken과 비교하여 일치하면 AccessToken 재발급, RefreshToken 재발급(RTR 방식)
 * 인증 성공 처리는 하지 않고 실패 처리
 */
@Slf4j
@RequiredArgsConstructor
public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private static final String[] NO_CHECK_URL = {
            "/swagger-ui/index.html", "/swagger-ui/springfox.css",
            "/swagger-ui/swagger-ui.css", "/swagger-ui/swagger-ui-standalone-preset.js",
            "/swagger-ui/springfox.js", "/swagger-ui/swagger-ui-bundle.js",
            "/swagger-resources/configuration/ui", "/swagger-ui/favicon-32x32.png",
            "/swagger-resources/configuration/security", "/swagger-resources",
            "/v3/api-docs", "api/v1/users/login", "/"
    };

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private final RedisTemplate<String, String> redisTemplate;

    private GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(request.getRequestURI());
        if (Arrays.stream(NO_CHECK_URL).anyMatch(s -> s.equals(request.getRequestURI()))) {
            filterChain.doFilter(request, response);
            return; // return으로 이후 현재 필터 진행 막기
        }


        // 해당 url 요청시 로그아웃 실행
        if (request.getRequestURI().equals("/api/v1/users/logout")) {
            jwtService.logout(request);
            log.info("로그아웃 성공");
            return;
        }


        // 리프레시 토큰이 요청 헤더에 존재했다면, 사용자가 AccessToken이 만료되어서
        // RefreshToken까지 보낸 것이므로 리프레시 토큰이 Redis의 리프레시 토큰과 일치하는지 판단 & 만료시간 검증 후,
        // 일치한다면 AccessToken을 재발급해준다.
        if (request.getRequestURI().equals("/api/v1/users/reissuance")) {
            String email = request.getHeader("X-User-Email");

            log.info("재발급 요청 email : {}", email);

            // 쿠키에서 RefreshToken 추출
            // -> RefreshToken이 없거나 유효하지 않다면(Redis에 저장된 RefreshToken과 다르다면) null을 반환
            String refreshToken = jwtService.extractRefreshToken(request)
                    .filter((token) -> jwtService.isRefreshTokenValid(token, email))
                    .orElse(null);

            if (refreshToken != null) {
                log.info("refreshToken 동일성 검증 통과");
                String reIssuedRefreshToken = reIssueRefreshToken(email);
                jwtService.sendRefreshToken(response, reIssuedRefreshToken);

                String reIssuedAccessToken = jwtService.createAccessToken(email);
                jwtService.sendAccessToken(response, email, reIssuedAccessToken);
                log.info("재발급 성공");
            }

            log.info("refreshToken 동일성 검증 실패");
            return;
        }

        // RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리하는 로직 수행
        // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
        // AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }


    /**
     * [리프레시 토큰 재발급 & Redis에 리프레시 토큰 업데이트 메소드]
     * jwtService.createRefreshToken()으로 리프레시 토큰 재발급 후
     * Redis에 재발급한 리프레시 토큰 업데이트
     */
    private String reIssueRefreshToken(String email) {
        String reIssuedRefreshToken = jwtService.createRefreshToken();
        log.info("refresh-token-re:{}", reIssuedRefreshToken);
        redisTemplate.opsForValue().set("RT:" + email, reIssuedRefreshToken);
        return reIssuedRefreshToken;
    }

    /**
     * [액세스 토큰 체크 & 인증 처리 메소드]
     * request에서 extractAccessToken()으로 액세스 토큰 추출 후, isTokenValid()로 유효한 토큰인지 검증
     * 유효한 토큰이면, 액세스 토큰에서 extractEmail로 Email을 추출한 후 findByEmail()로 해당 이메일을 사용하는 유저 객체 반환
     * 그 유저 객체를 saveAuthentication()으로 인증 처리하여
     * 인증 허가 처리된 객체를 SecurityContextHolder에 담기
     * 그 후 다음 인증 필터로 진행
     */
    public void checkAccessTokenAndAuthentication(HttpServletRequest request, HttpServletResponse response,
                                                  FilterChain filterChain) throws ServletException, IOException {
        String accessToken = jwtService.extractAccessToken(request)
                .filter(jwtService::isTokenValid)
                .orElse(null);
        String email = jwtService.extractEmail(accessToken);
        userRepository.findByEmail(email).ifPresent(this::saveAuthentication);
        filterChain.doFilter(request, response);
    }

    /**
     * [인증 허가 메소드]
     * 파라미터의 유저 : 우리가 만든 회원 객체 / 빌더의 유저 : UserDetails의 User 객체
     * new UsernamePasswordAuthenticationToken()로 인증 객체인 Authentication 객체 생성
     * <p>
     * UsernamePasswordAuthenticationToken의 파라미터
     * 1. 위에서 만든 UserDetailsUser 객체 (유저 정보)
     * 2. credential(보통 비밀번호로, 인증 시에는 보통 null로 제거)
     * 3. Collection < ? extends GrantedAuthority>로,
     * UserDetails의 User 객체 안에 Set<GrantedAuthority> authorities이 있어서 getter로 호출한 후에,
     * new NullAuthoritiesMapper()로 GrantedAuthoritiesMapper 객체를 생성하고 mapAuthorities()에 담기
     * <p>
     * SecurityContextHolder.getContext()로 SecurityContext를 꺼낸 후,
     * setAuthentication()을 이용하여 위에서 만든 Authentication 객체에 대한 인증 허가 처리
     */
    public void saveAuthentication(User myUser) {
        log.info("savedUserName:{}", myUser.getUserName());
        String password = myUser.getPassword();
        log.info("password:{}", password);
        if (password == null) { // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
            password = PasswordUtil.generateRandomPassword();
        }

        UserDetails userDetailsUser = org.springframework.security.core.userdetails.User.builder()
                .username(myUser.getEmail())
                .password(password)
                .roles(myUser.getUserRole().name())
                .build();
        log.info("author:{}", userDetailsUser.getAuthorities());


        Authentication authentication =
                new UsernamePasswordAuthenticationToken(userDetailsUser, null,
                        authoritiesMapper.mapAuthorities(userDetailsUser.getAuthorities()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
