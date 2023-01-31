package com.example.financialfinalproject.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class TradingDiary {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String created_at; // 주문시간
    private String uuid; // 주문 ID
    private String market; // 코인
    private String sid; // 매수/매도
    private Double volume; // 수량
    private Double paid_fee; // 수수료 (0.05%)
    private Integer bid_price; // 매수가격
    private Integer ask_price; // 매도가격
    private Double arbitrage; // 차익
    private Double revenue; // 수익률 (수수료반영)
    private String comment; // 메모

}
