package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.domain.request.UserJoinRequest;
import com.example.financialfinalproject.domain.request.UserPutRequest;
import com.example.financialfinalproject.domain.response.UserGetResponse;
import com.example.financialfinalproject.domain.response.UserJoinResponse;
import com.example.financialfinalproject.domain.response.UserPutResponse;
import com.example.financialfinalproject.domain.response.UserRoleResponse;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.global.jwt.service.JwtService;
import com.example.financialfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

    @Transactional(readOnly = true)
    public UserGetResponse getInfo(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
                });

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        String date = user.getRegisteredAt().format(formatter);
        return UserGetResponse.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .createAt(date)
                .build();

    }

    @Transactional
    public boolean validate(String email,String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
                });

        if (isWrongPassword(password, user))
            throw new AppException(INVALID_PASSWORD, INVALID_PASSWORD.getMessage());

        return true;
    }

    @Transactional
    public UserPutResponse modify(UserPutRequest request, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
                });

        user.updateUser(request.getUserName(),request.getImageUrl());

        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE;
        String date = user.getRegisteredAt().format(formatter);
        return UserPutResponse.builder()
                .userName(user.getUserName())
                .email(user.getEmail())
                .imageUrl(user.getImageUrl())
                .createAt(date)
                .build();
    }

    private boolean isWrongPassword(String password, User user) {
        return !encoder.matches(password, user.getPassword());
    }

}
