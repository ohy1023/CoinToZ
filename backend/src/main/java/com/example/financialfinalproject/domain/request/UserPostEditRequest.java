package com.example.financialfinalproject.domain.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserPostEditRequest {
    private String title;
    private String body;
}
