package com.example.financialfinalproject.domain.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPutResponse {
    private String userName;
    private String email;
    private String imageUrl;
    private String createAt;

    @Builder
    public UserPutResponse(String userName, String email, String imageUrl, String createAt) {
        this.userName = userName;
        this.email = email;
        this.imageUrl = imageUrl;
        this.createAt = createAt;
    }
}
