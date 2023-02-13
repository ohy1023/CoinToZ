package com.example.financialfinalproject.domain.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserGetResponse {
    private String userName;
    private String email;
    private String imageUrl;
    private String createAt;

    private boolean needUpbitKey;

    @Builder
    public UserGetResponse(String userName, String email, String imageUrl, String createAt, boolean needUpbitKey) {
        this.userName = userName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.createAt = createAt;
        this.needUpbitKey = needUpbitKey;
    }
}
