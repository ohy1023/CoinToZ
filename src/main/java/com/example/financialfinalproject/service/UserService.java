package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.dto.UserDto;
import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.domain.request.UserJoinRequest;
import com.example.financialfinalproject.domain.response.UserJoinResponse;
import com.example.financialfinalproject.domain.response.UserRoleResponse;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.global.jwt.service.JwtService;
import com.example.financialfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.example.financialfinalproject.domain.enums.UserRole.ADMIN;
import static com.example.financialfinalproject.domain.enums.UserRole.USER;
import static com.example.financialfinalproject.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    private final JwtService jwtService;

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Transactional
    public UserJoinResponse join(UserJoinRequest userJoinRequest) {
        userRepository.findByEmail(userJoinRequest.getEmail())
                .ifPresent((user -> {
                    throw new AppException(DUPLICATED_EMAIL, DUPLICATED_EMAIL.getMessage());
                }));
        User savedUser = userRepository.save(userJoinRequest.toEntity(encoder.encode(userJoinRequest.getPassword())));
        return UserJoinResponse.toResponse(savedUser);
    }

    @Transactional
    public String login(String email, String password) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage()));

        if (isWrongPassword(password, user))
            throw new AppException(INVALID_PASSWORD, INVALID_PASSWORD.getMessage());

        return jwtService.createAccessToken(user.getEmail());
    }

    @Transactional
    public UserRoleResponse changeRole(Integer userId, String email) {
        User admin = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
                });

        User targetUser = userRepository.findById(userId).orElseThrow(() -> {
            throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
        });

        if (admin.getUserRole() != ADMIN) {
            throw new AppException(INVALID_PERMISSION, INVALID_PERMISSION.getMessage());
        }

        if (targetUser.getUserRole() == USER) targetUser.promoteRole(targetUser);
        else if (targetUser.getUserRole() == ADMIN) targetUser.demoteRole(targetUser);

        return UserRoleResponse.toResponse(targetUser);
    }


//    public UserDto getUserByUserName(String userName) {
//        User user = userRepository.findByUserName(userName)
//                .orElseThrow(() -> new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage()));
//        return UserDto.toUserDto(user);
//    }

    private boolean isWrongPassword(String password, User user) {
        return !encoder.matches(password, user.getPassword());
    }

}
