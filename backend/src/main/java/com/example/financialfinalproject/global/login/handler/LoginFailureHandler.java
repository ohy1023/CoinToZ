package com.example.financialfinalproject.global.login.handler;

import com.example.financialfinalproject.domain.response.ErrorResponse;
import com.example.financialfinalproject.domain.response.Response;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.exception.ErrorCode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * JWT 로그인 실패 시 처리하는 핸들러
 * SimpleUrlAuthenticationFailureHandler를 상속받아서 구현
 */
@Slf4j
@RequiredArgsConstructor
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    // ObjectMapper: Java 객체를 JSON으로 변환하기 위한 ObjectMapper 인스턴스
    private final ObjectMapper objectMapper;

    /**
     * 인증 실패 시 호출되는 메서드
     * 인증이 실패하면 실패 이유를 ErrorResponse로 감싸서 클라이언트에 전달
     */
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException {
        // HTTP 응답 상태 코드를 400 (Bad Request)로 설정
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);

        // 응답의 Content-Type을 JSON 형식으로 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("utf-8");

        // 로그인 실패 시 사용할 ErrorResponse 객체 생성 (ErrorCode와 예외 메시지를 담음)
        ErrorResponse errorResponse = new ErrorResponse(ErrorCode.LOGIN_FAILED, exception.getMessage());

        // ErrorResponse 객체를 Response<ErrorResponse>로 감싸서 통일된 응답 형식으로 변환
        Response<ErrorResponse> responseBody = Response.error("ERROR", errorResponse);

        // ObjectMapper를 사용하여 Response 객체를 JSON으로 직렬화하고, 클라이언트에 응답으로 보냄
        objectMapper.writeValue(response.getWriter(), responseBody);

        // 로그인 실패 로그 기록
        log.info("로그인에 실패했습니다. 메시지 : {}", exception.getMessage());
    }
}
