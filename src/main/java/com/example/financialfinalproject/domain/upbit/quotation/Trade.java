package com.example.financialfinalproject.domain.upbit.quotation;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Trade { // 체결 정보
    private String ask_bid; // 매수 OR 매도;
    private double trade_price; // 채결 가격;
    private double trade_volume; // 채결량;
    private double change_price; // 변화량;

    }

