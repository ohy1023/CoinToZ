package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.upbit.OrderBook;
import com.example.financialfinalproject.domain.upbit.Ticker;
import com.example.financialfinalproject.domain.upbit.Trade;
import com.example.financialfinalproject.feign.UpbitFeignClient;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Service
public class UpbitService { // UpbitRestController

    private final UpbitFeignClient upbitFeignClient;

    public Ticker getTicker(String coin){ // 현재가 정보 - 1줄
        List<Ticker> tickerList = upbitFeignClient.getTicker("KRW-" + coin.toUpperCase());
        return tickerList.get(0);
    }

    public OrderBook getOrderBook(String coin){ // 호가 정보 - 리스트
        List<OrderBook> orderBookList = upbitFeignClient.getOrderBook("KRW-" + coin.toUpperCase());
        return orderBookList.get(0);
    }
    public List<Trade> getTrade(String coin, Integer count){ // 체결 정보 - 리스트

        List<Trade> tradeList = upbitFeignClient.getTrade("KRW-" + coin.toUpperCase(), count);

        return tradeList;
    }


}
