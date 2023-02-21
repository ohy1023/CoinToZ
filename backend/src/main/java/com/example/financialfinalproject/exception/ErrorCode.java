package com.example.financialfinalproject.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum ErrorCode{
    DUPLICATED_USER_NAME(HttpStatus.CONFLICT, "UserName이 중복됩니다."),

    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "Email이 중복됩니다."),

    USERNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 UserName이 없습니다."),

    EMAIL_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 Email이 없습니다."),
    INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "패스워드가 잘못되었습니다."),

    MISMATCH_PASSWORD(HttpStatus.CONFLICT, "패스워드가 일치하지 않습니다."),

    INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "잘못된 토큰입니다."),
    INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "사용자가 권한이 없습니다."),
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 포스트가 없습니다."),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "DB에러"),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 코맨트가 없습니다"),

    LIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "좋아요를 취소할려면 좋아요를 눌러주세요"),
    DISLIKE_NOT_FOUND(HttpStatus.NOT_FOUND, "싫어요를 취소할려면 싫어요를 눌러주세요"),
    DUPLICATED_LIKE_COUNT(HttpStatus.CONFLICT, "이미 좋아요를 눌렀습니다."),
    DUPLICATED_DISLIKE_COUNT(HttpStatus.CONFLICT, "이미 싫어요를 눌렀습니다."),

    // 매매일지
    COIN_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 코인이 없습니다."),
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 일지가 없습니다."),
    INVALID_AUTH(HttpStatus.UNAUTHORIZED, "인증번호가 잘못되었습니다.");

    private HttpStatus status;
    private String message;
}