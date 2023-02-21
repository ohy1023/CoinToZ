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
    private String ord_type; // 주문방식
    private String side; // 매수 매도
    private String created_at; //주문시간
    private String market; // 코인
    private Double price; // 매수가격
    private Double volume; // 수량
    private String comment; // 메모

    @Builder
    public TradingDiaryListDto(Long id, String ord_type, String side, String created_at, String market, Double price, Double volume, String comment) {
        this.id = id;
        this.ord_type = ord_type;
        this.side = side;
        this.created_at = created_at;
        this.market = market;
        this.price = price;
        this.volume = volume;
        this.comment = comment;
    }

    public static TradingDiaryListDto toDto(TradingDiary tradingDiary) {
        String createAt = tradingDiary.getCreated_at().format(DateTimeFormatter.ofPattern("MM월 dd일 HH:mm"));
        String orderType = tradingDiary.getOrd_type().equals("limit") ? "지정가" : "시장가";
        String sideType = tradingDiary.getSide().equals("bid") ? "매수" : "매도";

        return TradingDiaryListDto.builder()
                .id(tradingDiary.getId())
                .ord_type(orderType)
                .side(sideType)
                .volume(tradingDiary.getVolume())
                .market(tradingDiary.getMarket())
                .comment(tradingDiary.getComment())
                .price(tradingDiary.getPrice())
                .created_at(createAt)
                .build();
    }


}
