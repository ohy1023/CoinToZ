package com.example.financialfinalproject.domain.request;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserPutRequest {
    private String userName;
    private String imageUrl;

    @Builder
    public UserPutRequest(String userName, String imageUrl) {
        this.userName = userName;
        this.imageUrl = imageUrl;
    }
}
