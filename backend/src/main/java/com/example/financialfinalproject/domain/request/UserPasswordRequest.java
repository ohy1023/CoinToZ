package com.example.financialfinalproject.domain.request;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPasswordRequest {
    private String password;

    public UserPasswordRequest(String password) {
        this.password = password;
    }
}
