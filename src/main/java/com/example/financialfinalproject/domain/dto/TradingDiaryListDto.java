package com.example.financialfinalproject.domain.dto;


import com.example.financialfinalproject.domain.entity.TradingDiary;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.format.DateTimeFormatter;


@Slf4j
@Getter
@NoArgsConstructor
public class TradingDiaryListDto {
    private Long id;
    private String bid_created_at; // 매수 주문시간
    private String market; // 코인
    private Integer bid_price; // 매수가격
    private Integer ask_price; // 매도가격
    private Double volume; // 수량
    private Integer arbitrage; // 차익
    private Double revenue; // 수익률 (수수료반영)
    private String comment; // 메모

    @Builder
    public TradingDiaryListDto(Long id, String bid_created_at, String market, Integer bid_price, Integer ask_price, Double volume, Integer arbitrage, Double revenue, String comment) {
        this.id = id;
        this.bid_created_at = bid_created_at;
        this.market = market;
        this.bid_price = bid_price;
        this.ask_price = ask_price;
        this.volume = volume;
        this.arbitrage = arbitrage;
        this.revenue = revenue;
        this.comment = comment;
    }

    public static TradingDiaryListDto toDto(TradingDiary tradingDiary) {
        String bidCreateAt = tradingDiary.getBid_created_at().format(DateTimeFormatter.ofPattern("MM월 dd일 HH:mm"));

        return TradingDiaryListDto.builder()
                .id(tradingDiary.getId())
                .arbitrage(tradingDiary.getArbitrage())
                .bid_created_at(bidCreateAt)
                .ask_price(tradingDiary.getAsk_price())
                .bid_price(tradingDiary.getBid_price())
                .market(tradingDiary.getMarket())
                .comment(tradingDiary.getComment())
                .volume(tradingDiary.getVolume())
                .revenue(tradingDiary.getRevenue())
                .build();
    }

}
