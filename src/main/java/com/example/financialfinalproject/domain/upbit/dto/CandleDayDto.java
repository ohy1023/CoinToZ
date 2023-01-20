package com.example.financialfinalproject.domain.upbit.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CandleDayDto {
    private String market; //마켓명
    private String candle_date_time_utc; //	캔들 기준 시각(UTC 기준),  포맷: yyyy-MM-dd'T'HH:mm:ss
    private String candle_date_time_kst; //	캔들 기준 시각(KST 기준),  포맷: yyyy-MM-dd'T'HH:mm:ss
    private Double opening_price;	// 시가
    private Double high_price;	// 고가
    private Double low_price;	// 저가
    private Double trade_price; //	종가
    private Long timestamp; //	마지막 틱이 저장된 시각
    private Double candle_acc_trade_price; //	누적 거래 금액
    private Double candle_acc_trade_volume; //	누적 거래량
    private Double prev_closing_price; //	전일 종가(UTC 0시 기준)
    private Double change_price; //	전일 종가 대비 변화 금액
    private Double change_rate; //	전일 종가 대비 변화량
    private Double converted_trade_price; //	종가 환산 화폐 단위로 환산된 가격(요청에 convertingPriceUnit 파라미터 없을 시 해당 필드 포함되지 않음.)
}
