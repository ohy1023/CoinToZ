package com.example.financialfinalproject.domain.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserPostDeleteResponse {

    private String message;
    private Long postId;

}
