package com.example.financialfinalproject.global.login.service;

import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import static com.example.financialfinalproject.exception.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class LoginService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage()));

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getUserRole().name())
                .build();
    }
}
