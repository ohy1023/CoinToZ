package com.example.financialfinalproject.global.login.handler;

import com.example.financialfinalproject.domain.response.Response;
import com.example.financialfinalproject.domain.response.UserLoginResponse;
import com.example.financialfinalproject.global.jwt.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    // JWT 관련 로직을 처리하는 JwtService 객체 주입
    private final JwtService jwtService;

    // Redis를 사용하여 RefreshToken을 저장하는 데 사용하는 RedisTemplate 주입
    private final RedisTemplate<String, String> redisTemplate;

    // 응답을 JSON으로 변환하기 위한 ObjectMapper 주입
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        String email = extractUsername(authentication); // 인증 정보에서 email 추출
        String accessToken = jwtService.createAccessToken(email); // JwtService의 createAccessToken을 사용하여 AccessToken 발급
        String refreshToken = jwtService.createRefreshToken(); // JwtService의 createRefreshToken을 사용하여 RefreshToken 발급

        // 응답 상태 코드 설정 (200 OK)
        response.setStatus(HttpServletResponse.SC_OK);

        // Redis에 RefreshToken 저장 (Key: "RT:" + email)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), refreshToken);

        // JwtService에서 refreshToken을 HttpOnly 쿠기에 실어서 클라이언트로 보냄
        jwtService.sendRefreshToken(response, refreshToken);

        // JwtService에서 AccessToken을 응답 바디에 실어서 클라이언트로 보냄
        jwtService.sendAccessToken(response, email, accessToken);
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
