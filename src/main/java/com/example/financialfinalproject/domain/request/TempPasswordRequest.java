package com.example.financialfinalproject.domain.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TempPasswordRequest {
    private String code;
    private String email;

    @Builder
    public TempPasswordRequest(String code, String email) {
        this.code = code;
        this.email = email;
    }
}
