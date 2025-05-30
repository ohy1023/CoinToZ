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
import java.util.List;

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

    private final JwtService jwtService;
    private final UserRepository userRepository;

    private final RedisTemplate<String, String> redisTemplate;

    private final GrantedAuthoritiesMapper authoritiesMapper = new NullAuthoritiesMapper();

    private static final List<String> NO_CHECK_URLS = List.of(
            "/api/v1/users/login",
            "/api/v1/users/join",
            "/api/v1/emails/",
            "/v3/api-docs",
            "/swagger",
            "/swagger-ui",
            "/swagger-resources",
            "/css", "/js", "/images", "/favicon.ico", "/webjars"
    );

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info(request.getRequestURI());

        // 인증이 필요 없는 URL의 경우 JWT 필터를 건너뜀
        if (isNoCheckPath(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        // 해당 url 요청시 로그아웃 실행
        if (request.getRequestURI().equals("/api/v1/users/logout")) {
            jwtService.logout(request, response);
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

            // 동일성 검증 투과 여부 체크
            if (refreshToken != null) {
                log.info("refreshToken 동일성 검증 통과");

                // 응답 상태 코드 설정 (200 OK)
                response.setStatus(HttpServletResponse.SC_OK);

                // RefreshToken 재발급 및 HTTP ONLY 쿠키 설정
                String reIssuedRefreshToken = reIssueRefreshToken(email);
                jwtService.sendRefreshToken(response, reIssuedRefreshToken);

                // Redis에 재발급 받은 RefreshToken 저장
                jwtService.saveRefreshTokenInRedis(email, reIssuedRefreshToken);

                // AccessToken 재발급 및 응답 바디에 AccessToken 반환
                String reIssuedAccessToken = jwtService.createAccessToken(email);
                jwtService.sendAccessToken(response, email, reIssuedAccessToken);

                log.info("재발급 성공");
            } else {
                log.warn("refreshToken 검증 실패: 유효하지 않거나 Redis 정보와 일치하지 않음");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            }

            return;
        }

        // RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리하는 로직 수행
        // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 401 에러 발생
        // AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공
        checkAccessTokenAndAuthentication(request, response, filterChain);
    }

    /**
     * 인증 처리를 건너뛸 경로를 확인하는 메서드
     * - permitAll()로 설정된 URL 경로
     * - GET /api/v1/posts/{postId}/comments 와 같이 인증이 필요 없는 경로에 대해
     * JWT 필터를 타지 않고 바로 다음 필터로 넘기기 위함
     */
    private boolean isNoCheckPath(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        return NO_CHECK_URLS.stream().anyMatch(uri::startsWith) // 허용된 경로(prefix 매칭)
                || (uri.equals("/api/v1/posts") && method.equals("GET")) // 게시글 목록 조회
                || (uri.matches("/api/v1/posts/\\d+/comments") && method.equals("GET")); // 댓글 조회(GET)은 예외 처리
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
        try {
            jwtService.extractAccessToken(request)
                    .filter(jwtService::isTokenValid)
                    .ifPresent(accessToken -> {
                        String email = jwtService.extractEmail(accessToken);
                        userRepository.findByEmail(email)
                                .ifPresent(this::saveAuthentication);
                    });
        } catch (Exception e) {
            // 필요시 로그만 남기고 무시 (비회원 접근이 가능할 수 있으므로)
            log.warn("AccessToken 인증 실패: {}", e.getMessage());
        }

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
