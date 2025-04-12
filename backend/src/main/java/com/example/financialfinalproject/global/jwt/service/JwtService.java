package com.example.financialfinalproject.global.jwt.service;

import com.example.financialfinalproject.domain.response.Response;
import com.example.financialfinalproject.domain.response.UserLoginResponse;
import com.example.financialfinalproject.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

@Slf4j
@Getter
@Service
@RequiredArgsConstructor
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    @Value("${jwt.access.header}")
    private String accessHeader;

    @Value("${jwt.refresh.header}")
    private String refreshHeader;

    /**
     * JWT의 Subject와 Claim으로 email 사용 -> 클레임의 name을 "email"으로 설정
     * JWT의 헤더에 들어오는 값 : 'Authorization(Key) = Bearer {토큰} (Value)' 형식
     */
    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;
    private final ObjectMapper objectMapper;
    private final RedisTemplate<String, String> redisTemplate;

    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(String email) {
        log.info(email);
        Claims claims = Jwts.claims();
        claims.put("email", email);
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(ACCESS_TOKEN_SUBJECT)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + accessTokenExpirationPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * RefreshToken 생성
     * RefreshToken은 Claim에 email도 넣지 않으므로 setClaim() X
     */
    public String createRefreshToken() {
        return Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * AccessToken 바디에 실어서 보내기
     */
    public void sendAccessToken(HttpServletResponse response, String email, String accessToken) throws IOException {
        // 응답 형식을 JSON으로 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        // 클라이언트에 응답할 데이터(UserLoginResponse 객체 생성)
        UserLoginResponse userLoginResponse = new UserLoginResponse(email, accessToken);

        // 성공 응답을 Response<UserLoginResponse>로 감싸서 처리
        Response<UserLoginResponse> responseBody = Response.success(userLoginResponse);

        // ObjectMapper를 사용하여 JSON으로 직렬화 후 클라이언트에 응답
        objectMapper.writeValue(response.getWriter(), responseBody);
        log.info("Access Token -> Body 설정 완료");
    }

    public void sendRefreshToken(HttpServletResponse response, String refreshToken) {

        // RefreshToken은 HttpOnly 쿠키로 저장
        ResponseCookie cookie = ResponseCookie.from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite(SameSite.NONE.attributeValue())
                .domain("cointoz.store")
                .path("/")
                .maxAge(refreshTokenExpirationPeriod / 1000)
                .build();

        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        log.info("Refresh Token -> 쿠키 설정 완료.");
    }

    /**
     * 쿠키에서 RefreshToken 추출
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        if (request.getCookies() == null) return Optional.empty();

        return Arrays.stream(request.getCookies())
                .filter(cookie -> cookie.getName().equals("refreshToken"))
                .map(Cookie::getValue)
                .findFirst();
    }

    /**
     * 헤더에서 AccessToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(accessHeader -> accessHeader.startsWith(BEARER))
                .map(accessHeader -> accessHeader.replace(BEARER, ""));
    }

    /**
     * AccessToken에서 Email 추출
     **/
    public String extractEmail(String token) {
        try {
            Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                    .getBody();
            log.info("token info:{}", body);
            return body.get("email", String.class);
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return null;
        }

    }

    /**
     * RefreshToken Redis 저장 & 업데이트
     */
    @Transactional
    public void saveRefreshTokenInRedis(String email, String refreshToken) {
        log.info("email:{}", email);
        log.info("update:{}", refreshToken);
        redisTemplate.opsForValue().set("RT:" + email, refreshToken, Duration.ofMillis(refreshTokenExpirationPeriod));
    }

    /**
     * AccessToken 타당성 검증
     * 만료되었는지 검증 & Redis를 통해 로그아웃된 토큰인지 검증
     */
    public boolean isTokenValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
            if (logoutValueOperations.get("blackList:" + token) != null) {
                log.info("로그아웃 된 토큰입니다.");
                return false;
            }
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * RefreshToken 타당성 검증
     * 만료되었는지 검증 & Redis에 저장된 토큰과 동일성 검증
     */
    public boolean isRefreshTokenValid(String token, String email) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            String dbToken = redisTemplate.opsForValue().get("RT:" + email);
            return dbToken != null && dbToken.equals(token) && !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 만료시간 가져오기
     */
    public Date getExpiredTime(String token) {
        Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return body.getExpiration();
    }

    /**
     * 로그아웃
     * AccessToken 남은 만료시간 계산 후 레디스에 블랙리스트 저장
     * 레디스에 저장된 RefreshToken 삭제
     */
    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response) {
        String accessToken = extractAccessToken(request)
                .filter((this::isTokenValid))
                .orElse(null);
        String email = extractEmail(accessToken);
        long expiredAccessTokenTime = getExpiredTime(accessToken).getTime() - new Date().getTime();
        redisTemplate.opsForValue().set("blackList:" + accessToken, email, Duration.ofMillis(expiredAccessTokenTime));
        redisTemplate.delete("RT:" + email); // Redis에서 유저 리프레시 토큰 삭제

        ResponseCookie deleteCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .sameSite(SameSite.NONE.attributeValue())
                .maxAge(0)  // <== 만료시간 0으로 설정해서 삭제
                .domain("cointoz.store")
                .build();

        // 응답 상태 코드 설정 (200 OK)
        response.setStatus(HttpServletResponse.SC_OK);

        response.addHeader("Set-Cookie", deleteCookie.toString());
    }
}