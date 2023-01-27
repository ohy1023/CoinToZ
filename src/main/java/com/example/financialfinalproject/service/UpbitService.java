package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.upbit.candle.CandleDayDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleMinuteDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleMonthDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleWeekDto;
import com.example.financialfinalproject.domain.upbit.exchange.Acount;
import com.example.financialfinalproject.domain.upbit.exchange.OrderRequest;
import com.example.financialfinalproject.domain.upbit.exchange.OrderResponse;
import com.example.financialfinalproject.domain.upbit.exchange.UpbitToken;
import com.example.financialfinalproject.domain.upbit.quotation.MarketDto;
import com.example.financialfinalproject.domain.upbit.quotation.OrderBook;
import com.example.financialfinalproject.domain.upbit.quotation.Ticker;
import com.example.financialfinalproject.domain.upbit.quotation.Trade;
import com.example.financialfinalproject.feign.UpbitFeignClient;
import com.example.financialfinalproject.global.jwt.service.UpbitJwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;

@RequiredArgsConstructor
@Getter
@Service
public class UpbitService {

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

    public List<Acount> getAcount(String accessKey, String secretKey){ // 전체계좌조회
        UpbitToken upbitToken = upbitJwtService.getToken(accessKey,secretKey);
        List<Acount> acounts = upbitFeignClient.getAcount(upbitToken.getUpbitToken());
        return acounts;
    }

    //주문하는 Token 생성
    public OrderResponse getOrder(String accessKey, String secretKey, OrderRequest orderRequest) throws UnsupportedEncodingException, NoSuchAlgorithmException, JsonProcessingException {
        UpbitToken upbitToken = upbitJwtService.getOrderToken(accessKey,secretKey,orderRequest);

        HashMap<String, String> params = new HashMap<>();
        params.put("market", orderRequest.getMarket());
        params.put("side", orderRequest.getSide());
        params.put("volume", String.valueOf(orderRequest.getVolume()));
        params.put("price", String.valueOf(orderRequest.getPrice()));
        params.put("ord_type", orderRequest.getOrd_type());


//        ObjectMapper objectMapper = new ObjectMapper();
//        String json = objectMapper.writeValueAsString(orderRequest);
//        System.out.println(json);

        OrderResponse orderResponse = upbitFeignClient.getOrder(upbitToken.getUpbitToken(),params);


//                orderRequest.getMarket(),
//                orderRequest.getSide(),
//                orderRequest.getVolume(),
//                orderRequest.getPrice(),
//                orderRequest.getOrd_type());

        return orderResponse;
    }
}
