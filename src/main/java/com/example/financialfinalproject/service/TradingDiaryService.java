package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.entity.TradingDiary;
import com.example.financialfinalproject.domain.upbit.exchange.OrderResponse;
import com.example.financialfinalproject.repository.TradingDiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class TradingDiaryService {

private final TradingDiaryRepository tradingDiaryRepository;

public void write(OrderResponse orderResponse) {

    // 수수료 반올림
    int fee = (int) Math.round(orderResponse.getPaid_fee());

    // 매수
    if (orderResponse.getSide().equals("bid")) {

        TradingDiary tradingDiary = TradingDiary.builder()
                .created_at(orderResponse.getCreated_at())
                .uuid(orderResponse.getUuid())
                .market(orderResponse.getMarket())
                .sid(orderResponse.getSide())
                .volume(orderResponse.getVolume())
                .paid_fee(orderResponse.getPaid_fee())
                .bid_price(orderResponse.getPrice() + fee)
                .build();

        tradingDiaryRepository.save(tradingDiary);
    }

    if (orderResponse.getSide().equals("ask")) {


    }



