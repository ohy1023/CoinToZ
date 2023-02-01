package com.example.financialfinalproject.domain.upbit.exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CoinWithDrawRequest {
    private String currency; // Currency 코드
    private String amount;  // 출금 수량
    private String address; // 출금 가능 주소에 등록된 출금 주소
    private String secondary_address; // 2차 출금 주소
    private String transaction_type; // 출금 유형(default : 일반출금, internal : 바로출금)
}
