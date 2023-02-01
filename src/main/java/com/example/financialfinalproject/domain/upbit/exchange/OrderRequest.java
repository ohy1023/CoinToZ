package com.example.financialfinalproject.domain.upbit.exchange;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class OrderRequest{

private String market; // 마켓 Id
private String side;  // 주문 종류 bid(매수), ask(매도)
private Double volume; // 주문량
private Double price; // 주문가격
private String ord_type; // 주문타입 (limit(지정가), price(매수), market(매도))
}
