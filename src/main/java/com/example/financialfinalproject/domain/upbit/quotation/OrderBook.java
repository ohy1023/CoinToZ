package com.example.financialfinalproject.domain.upbit.quotation;

import lombok.Getter;

import java.util.List;

@Getter
public class OrderBook <T> {
    private String market; // 마켓코드
    private Long timestamp; // 호가 생성 시각
    private double total_ask_size; // 호가 매도 총 잔량
    private double total_bid_size; // 호가 매수 총 잔량
    private List<orderbook_units> orderbook_units;

    @Getter
    public static class orderbook_units { // 호가 정보
        public double bid_price; // 매수호가
        public double bid_size;  // 매수 잔량
        public double ask_price; // 매도호가
        public double ask_size; // 매도잔량
    }
}
