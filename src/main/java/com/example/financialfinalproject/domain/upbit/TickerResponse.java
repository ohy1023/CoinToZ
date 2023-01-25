package com.example.financialfinalproject.domain.upbit;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TickerResponse {
    private String change; // 시세상황
    private String gap_price; // 시세상황
    private String trade_price; // 종가:현재가
    private String prev_closing_price; // 전일 가격
    private String high_price; // 고가:최고가격
    private String low_price; // 저가:최저가격
}
