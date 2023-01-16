package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.dto.UserDto;
import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.domain.request.UserJoinRequest;
import com.example.financialfinalproject.domain.response.UserJoinResponse;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.repository.UserRepository;
import com.example.financialfinalproject.utils.JwtUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import static com.example.financialfinalproject.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final BCryptPasswordEncoder encoder;

    @Value("${jwt.secret}")
    private String secretKey;

    public UserJoinResponse join(UserJoinRequest userJoinRequest) {
        userRepository.findByUserName(userJoinRequest.getUserName())
                .ifPresent((user -> {
                    throw new AppException(DUPLICATED_USER_NAME, DUPLICATED_USER_NAME.getMessage());
                }));
        User savedUser = userRepository.save(userJoinRequest.toEntity(encoder.encode(userJoinRequest.getPassword())));
        return UserJoinResponse.toResponse(savedUser);
    }

    public String login(String userName, String password) {

        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(USERNAME_NOT_FOUND, USERNAME_NOT_FOUND.getMessage()));

        if (isWrongPassword(password, user))
            throw new AppException(INVALID_PASSWORD, INVALID_PASSWORD.getMessage());

        long expiredTimeMs = 1000 * 60 * 60L;
        return JwtUtils.createToken(userName, secretKey, expiredTimeMs);
    }

    public UserDto getUserByUserName(String userName) {
        User user = userRepository.findByUserName(userName)
                .orElseThrow(() -> new AppException(USERNAME_NOT_FOUND, USERNAME_NOT_FOUND.getMessage()));
        return UserDto.toUserDto(user);
    }

    private boolean isWrongPassword(String password, User user) {
        return !encoder.matches(password, user.getPassword());
    }

}
