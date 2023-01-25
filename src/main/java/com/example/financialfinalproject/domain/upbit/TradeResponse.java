package com.example.financialfinalproject.domain.upbit;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class TradeResponse { // 체결표 응답

    private String tradeTime; // 체결 시간
    private String ask_bid; // 매수 OR 매도
    private double CoinPrice; // 체결가격 = 코인 가격
    private double tradeVolume; // 체결량
    private String tradePrice; // 체결금액

}
