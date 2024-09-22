package com.example.financialfinalproject.global.login.handler;

import com.example.financialfinalproject.domain.response.Response;
import com.example.financialfinalproject.domain.response.UserLoginResponse;
import com.example.financialfinalproject.global.jwt.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
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

        // JwtService에서 AccessToken과 RefreshToken을 응답 헤더에 실어서 클라이언트로 보냄
        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        // Redis에 RefreshToken 저장 (Key: "RT:" + email)
        redisTemplate.opsForValue()
                .set("RT:" + authentication.getName(), refreshToken);

        // 응답 상태 코드 설정 (200 OK)
        response.setStatus(HttpServletResponse.SC_OK);

        // 응답 형식을 JSON으로 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        // 클라이언트에 응답할 데이터(UserLoginResponse 객체 생성)
        UserLoginResponse userLoginResponse = new UserLoginResponse(email, accessToken, refreshToken);

        // 성공 응답을 Response<UserLoginResponse>로 감싸서 처리
        Response<UserLoginResponse> responseBody = Response.success(userLoginResponse);

        // ObjectMapper를 사용하여 JSON으로 직렬화 후 클라이언트에 응답
        objectMapper.writeValue(response.getWriter(), responseBody);
    }

    private String extractUsername(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userDetails.getUsername();
    }
}
