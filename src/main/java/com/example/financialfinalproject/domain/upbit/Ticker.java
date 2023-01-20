package com.example.financialfinalproject.domain.upbit;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Ticker { // 현재가 정보

    private double trade_price; // 종가:현재가
    private double opening_price; // 시가:시작가격
    private double low_price; // 저가:최저가격
    private double high_price; // 고가:최고가격

    private String trade_date_kst; // 최근 거래 일자
    private String trade_time_kst; // 최근 거래 시각

    private double prev_closing_price; // 전일 가격
    private double trade_volume; // 최근 거래량
    private String change; // 시세상황



}
