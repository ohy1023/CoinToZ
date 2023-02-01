package com.example.financialfinalproject.domain.upbit.exchange;

import lombok.*;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class OrderResponse {
    private String uuid; // 주문 ID
    private String market; // 마켓 ID(KRW-BTC)
    private String side;  // 주문 종류 bid(매수), ask(매도)
    private Double volume; // 주문량
    private Integer price; // 주문가격
    private String ord_type; // 주문타입 (limit(지정가), price(매수), market(매도))
    private String created_at; // 주문생성시간
    private Double paid_fee; // 수수료
    private String executed_volume;// 체결된 양

}
