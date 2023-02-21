package com.example.financialfinalproject.domain.upbit.candle;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class CandleMinuteDto {
    private String market; //마켓명
    private String	candle_date_time_utc; //캔들 기준 시각(UTC 기준), 포맷: yyyy-MM-dd'T'HH:mm:ss
    private String candle_date_time_kst; //	캔들 기준 시각(KST 기준), 포맷: yyyy-MM-dd'T'HH:mm:ss
    private Double opening_price; //시가
    private Double high_price; //고가
    private Double low_price; //저가
    private Double trade_price; //종가
    private Long timestamp; //해당 캔들에서 마지막 틱이 저장된 시각
    private Double candle_acc_trade_price; //누적 거래 금액
    private Double candle_acc_trade_volume; //누적 거래량
    private Integer unit; //분 단위(유닛)
}
