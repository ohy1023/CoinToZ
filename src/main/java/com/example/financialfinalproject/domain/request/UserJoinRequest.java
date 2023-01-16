package com.example.financialfinalproject.domain.request;

import com.example.financialfinalproject.domain.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserJoinRequest {

    private String userName;
    private String password;

    public User toEntity(String password) {
        return User.builder()
                .userName(this.userName)
                .password(password)
                .build();
    }

    @Builder
    public UserJoinRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
