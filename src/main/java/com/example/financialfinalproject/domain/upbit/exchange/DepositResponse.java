package com.example.financialfinalproject.domain.upbit.exchange;

import lombok.Getter;

@Getter
public class DepositResponse {

private String type; // 입출금 종류
private String uuid; // 고유 ID
private String currency; // 화폐단위
private String txid; // 트랜잭션 ID
private String state; // 임금상태
private String created_at; // 입금생성시간
private String done_at; // 임금완료시간
private String aomunt; // 임금 금액,수량
private String fee; // 입금수수료
private String transaction_type; // 트랜잭션 유형
}
