package com.example.financialfinalproject.global.oauth2.handler;

import com.example.financialfinalproject.global.jwt.service.JwtService;
import com.example.financialfinalproject.global.oauth2.CustomOAuth2User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Slf4j
@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtService jwtService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        log.info("OAuth2 Login 성공!");
        try {
            CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

            loginSuccess(request, response, oAuth2User); // 로그인에 성공한 경우 access, refresh 토큰 생성

        } catch (Exception e) {
            throw e;
        }

    }

    private void loginSuccess(HttpServletRequest request, HttpServletResponse response, CustomOAuth2User oAuth2User) throws IOException {
        log.info("getEmail:{}", oAuth2User.getEmail());
        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken();

        response.addHeader(jwtService.getAccessHeader(), "Bearer " + accessToken);
        response.addHeader(jwtService.getRefreshHeader(), "Bearer " + refreshToken);

        // AccessToken JSON 바디로 전달 (SPA가 메모리에 저장하도록)
        jwtService.sendAccessToken(response, oAuth2User.getEmail(), accessToken);

        // RefreshToken HttpOnly 쿠키로 설정
        jwtService.sendRefreshToken(response, refreshToken);

        jwtService.saveRefreshToken(oAuth2User.getEmail(), refreshToken);

        String targetUrl = UriComponentsBuilder.fromUriString("https://api.cointoz.store/login")
                .queryParam("accessToken", accessToken)
                .queryParam("refreshToken", refreshToken)
                .queryParam("email", oAuth2User.getEmail())
                .build().toUriString();

        getRedirectStrategy().sendRedirect(request, response, targetUrl);


    }
}