package com.example.financialfinalproject.domain.upbit.exchange;
import lombok.*;

import java.util.List;
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Setter
public class OrderOneResponse {

    private String uuid; // 주문 ID
    private String market; // 마켓 ID(KRW-BTC)
    private String side;  // 주문 종류 bid(매수), ask(매도)
    private Double volume; // 주문량
    private Integer price; // 주문가격
    private String ord_type; // 주문타입 (limit(지정가), price(매수), market(매도))
    private String created_at; // 주문생성시간
    private Double paid_fee; // 수수료
    private String executed_volume;// 체결된 양
    private String locked; // 거래비용
    private List<trades> trades;

    @Getter
    public static class trades { // 호가 정보
        private String uuid; // 주문 ID
        private String market; // 마켓 ID(KRW-BTC)
        private String side;  // 주문 종류 bid(매수), ask(매도)
        private Double volume; // 주문량
        private Double price; // 주문가격
        private Double funds; // 체결 총 가격

    }

    }



