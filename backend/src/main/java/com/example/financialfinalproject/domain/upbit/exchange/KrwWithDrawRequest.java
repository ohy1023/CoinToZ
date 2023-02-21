package com.example.financialfinalproject.domain.upbit.exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KrwWithDrawRequest {
    private String amount;  // 출금액
    private String two_factor_type; // 	2차 인증 수단(default : kakao_pay, naver)
}
