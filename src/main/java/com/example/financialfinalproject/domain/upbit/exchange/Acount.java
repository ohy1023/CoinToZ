package com.example.financialfinalproject.domain.upbit.exchange;

import lombok.Getter;

@Getter
public class Acount {

private String currency; // 화폐단위
private String balance; // 주문가능 금액/수량
private String locked; // 주문 중 묶여 있는 금액/수량
private String avg_buy_price; // 매수평균가
private Boolean avg_buy_price_modified; // 매수평균가 수정여부
private String unit_currency;// 평단가 기준 화폐

}
