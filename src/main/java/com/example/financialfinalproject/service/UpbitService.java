package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.upbit.*;
import com.example.financialfinalproject.domain.upbit.candle.CandleDayDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleMinuteDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleMonthDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleWeekDto;
import com.example.financialfinalproject.feign.UpbitFeignClient;
import com.example.financialfinalproject.global.jwt.service.UpbitJwtService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Getter
@Service
public class UpbitService { // UpbitRestController

    private final UpbitFeignClient upbitFeignClient;
    private final UpbitJwtService upbitJwtService;

    public List<MarketDto> getMarket(Boolean isDetails) {
        return upbitFeignClient.getMarKet(isDetails);
    }

    public List<CandleMinuteDto> getCandlesMinute(Integer unit, String market, String to, String count) {
        return upbitFeignClient.getCandlesMinute(unit, market, to, count);
    }

    public List<CandleDayDto> getCandlesDay(String market, String to, String count, String convertingPriceUnit) {
        return upbitFeignClient.getCandlesDay(market, to, count, convertingPriceUnit);
    }

    public List<CandleWeekDto> getCandlesWeek(String market, String to, String count) {
        return upbitFeignClient.getCandlesWeek(market, to, count);
    }

    public List<CandleMonthDto> getCandlesMonth(String market, String to, String count) {
        return upbitFeignClient.getCandlesMonth(market, to, count);
    }


    public Ticker getTicker(String coin) { // 현재가 정보 - 1줄
        List<Ticker> tickerList = upbitFeignClient.getTicker("KRW-" + coin.toUpperCase());
        return tickerList.get(0);
    }

    public OrderBook getOrderBook(String coin) { // 호가 정보 - 리스트
        List<OrderBook> orderBookList = upbitFeignClient.getOrderBook("KRW-" + coin.toUpperCase());
        return orderBookList.get(0);
    }


    public List<Trade> getTrade(String coin, Integer count) { // 체결 정보 - 리스트

        List<Trade> tradeList = upbitFeignClient.getTrade("KRW-" + coin.toUpperCase(), count);

        return tradeList;
    }

    public UpbitToken getToken(String accessKey, String secretKey){
    UpbitToken upbitToken = upbitJwtService.getToken(accessKey,secretKey);
    return upbitToken;
    }

    public List<Acount> getAcount(String token){
        List<Acount> acounts = upbitFeignClient.getAcount(token);
        return acounts;
    }


}
