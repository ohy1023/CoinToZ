package com.example.financialfinalproject.domain.upbit;

import lombok.Getter;

@Getter
public class Acount {

private String currency;
private String balance;
private String locked;
private String avg_buy_price;
private Boolean avg_buy_price_modified;
private String unit_currency;

}
