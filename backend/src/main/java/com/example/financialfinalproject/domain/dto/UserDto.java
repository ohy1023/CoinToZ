package com.example.financialfinalproject.domain.dto;

import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.domain.enums.UserRole;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserDto {
    private Integer id;
    private String userName;
    private String password;
    private UserRole userRole;

    @Builder
    public UserDto(Integer id, String userName, String password, UserRole userRole) {
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.userRole = userRole;
    }

    public static UserDto toUserDto(User user) {
        return UserDto.builder()
                .id(user.getId())
                .userName(user.getUserName())
                .password(user.getPassword())
                .userRole(user.getUserRole())
                .build();
    }
}
