package com.example.financialfinalproject.domain.upbit.exchange;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class WithDraw {
    private String type; //	입출금 종류
    private String uuid; //	출금의 고유 아이디
    private String currency; //	화폐를 의미하는 영문 대문자 코드
    private String txid; //	출금의 트랜잭션 아이디
    private String state; //	출금 상태 (WAITING : 대기중, PROCESSING : 진행중, DONE : 완료, FAILED : 실패, CANCELLED : 취소됨, REJECTED : 거절됨
    private String created_at; //	출금 생성 시간
    private String done_at; //	출금 완료 시간
    private String amount; //	출금 금액/수량
    private String fee; //	출금 수수료
    private String transaction_type; //	출금 유형(default : 일반출금, internal : 바로출금)
}
