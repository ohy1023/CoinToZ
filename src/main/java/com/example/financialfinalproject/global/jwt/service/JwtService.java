package com.example.financialfinalproject.global.jwt.service;

import com.example.financialfinalproject.repository.UserRepository;
import io.jsonwebtoken.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;
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
    private static final String EMAIL_CLAIM = "email";
    private static final String BEARER = "Bearer ";

    private final UserRepository userRepository;

    private final RedisTemplate<String, String> redisTemplate;

    /**
     * AccessToken 생성 메소드
     */
    public String createAccessToken(String email) {
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
     * RefreshToken은 Claim에 email도 넣지 않으므로 withClaim() X
     */
    public String createRefreshToken() {
        return Jwts.builder()
                .setSubject(REFRESH_TOKEN_SUBJECT)
                .setExpiration(new Date(System.currentTimeMillis() + refreshTokenExpirationPeriod))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    /**
     * AccessToken 헤더에 실어서 보내기
     */
    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        response.setHeader(accessHeader, accessToken);
        log.info("재발급된 Access Token : {}", accessToken);
    }

    /**
     * AccessToken + RefreshToken 헤더에 실어서 보내기
     */
    public void sendAccessAndRefreshToken(HttpServletResponse response, String accessToken, String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);

        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
        log.info("Access Token, Refresh Token 헤더 설정 완료");
    }

    /**
     * 헤더에서 RefreshToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(refreshHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * 헤더에서 AccessToken 추출
     * 토큰 형식 : Bearer XXX에서 Bearer를 제외하고 순수 토큰만 가져오기 위해서
     * 헤더를 가져온 후 "Bearer"를 삭제(""로 replace)
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(accessHeader))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    /**
     * AccessToken에서 Email 추출
     **/
    public String extractEmail(String token) {
        try {
            Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token)
                    .getBody();
            log.info("token info:{}",body);
            return body.get("email", String.class);
        } catch (Exception e) {
            log.error("액세스 토큰이 유효하지 않습니다.");
            return null;
        }

    }

    /**
     * AccessToken 헤더 설정
     */
    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(accessHeader, accessToken);
    }

    /**
     * RefreshToken 헤더 설정
     */
    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(refreshHeader, refreshToken);
    }

    /**
     * RefreshToken DB 저장(업데이트)
     */
    @Transactional
    public void updateRefreshToken(String email, String refreshToken) {
        log.info("email:{}", email);
        log.info("update:{}", refreshToken);
        redisTemplate.opsForValue().set("RT:" + email, refreshToken);

    }

    public boolean isTokenValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token);
            log.info("로그아웃 테스트 :{}",token);
            ValueOperations<String, String> logoutValueOperations = redisTemplate.opsForValue();
            if (logoutValueOperations.get("blackList "+token) != null) {
                log.info("로그아웃 된 토큰입니다.");
                return false;
            }
            return !claims.getBody().getExpiration().before(new Date());
        } catch (Exception e) {
            return false;
        }
    }

    public Date getExpiredTime(String token) {
        Claims body = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return body.getExpiration();
    }

    @Transactional
    public void logout(HttpServletRequest request) {
        String accessToken = extractAccessToken(request)
                .filter((this::isTokenValid))
                .orElse(null);
        String email = extractEmail(accessToken);
        long expiredAccessTokenTime = getExpiredTime(accessToken).getTime() - new Date().getTime();
        redisTemplate.opsForValue().set("blackList " + accessToken, email, Duration.ofMillis(expiredAccessTokenTime));
        redisTemplate.delete("RT:" + email); // Redis에서 유저 리프레시 토큰 삭제
    }

}
