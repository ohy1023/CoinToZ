package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.domain.upbit.candle.CandleDayDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleMinuteDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleMonthDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleWeekDto;
import com.example.financialfinalproject.domain.upbit.exchange.*;
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

    private final TradingDiaryService tradingDiaryService;


    // QUOTATION API



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


    // EXCHANGE API


    // 계좌조회

    public List<Acount> getAcount(String accessKey, String secretKey){ // 전체계좌조회
        UpbitToken upbitToken = upbitJwtService.getToken(accessKey,secretKey);
        List<Acount> acounts = upbitFeignClient.getAcount(upbitToken.getUpbitToken());
        return acounts;
    }

    // 주문
    public OrderResponse getOrder(String accessKey, String secretKey, OrderRequest orderRequest, User user) throws UnsupportedEncodingException, NoSuchAlgorithmException, JsonProcessingException {
        UpbitToken upbitToken = upbitJwtService.getOrderToken(accessKey,secretKey,orderRequest);

        HashMap<String, String> params = new HashMap<>();
        params.put("market", orderRequest.getMarket());
        params.put("side", orderRequest.getSide());
        params.put("volume", String.valueOf(orderRequest.getVolume()));
        params.put("price", String.valueOf(orderRequest.getPrice()));
        params.put("ord_type", orderRequest.getOrd_type());

        OrderResponse orderResponse = upbitFeignClient.getOrder(upbitToken.getUpbitToken(),params);

        tradingDiaryService.write(orderResponse,user);

        return orderResponse;
    }

    // 주문리스트
    public List<OrderResponse> getOrderList(String accessKey, String secretKey, String state, User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UpbitToken upbitToken = upbitJwtService.getOrderListToken(accessKey,secretKey,state);
        List<OrderResponse> orderResponses = upbitFeignClient.getOrderList(upbitToken.getUpbitToken(),state);
        //tradingDiaryService.write(orderResponses.get(0),user);
        return orderResponses;
    }


    // 주문취소
    public OrderDeleteResponse getOrderDelete(String accessKey, String secretKey, String uuid) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UpbitToken upbitToken = upbitJwtService.getOrderDeleteToken(accessKey,secretKey,uuid);
        OrderDeleteResponse orderResponse = upbitFeignClient.getOrderDelete(upbitToken.getUpbitToken(),uuid);
        tradingDiaryService.orderDelete(orderResponse);

        return orderResponse;
    }


    // 코인 출금하기
    public CoinWithDrawResponse askWithdrawCoin(String accessKey, String secretKey, CoinWithDrawRequest coinWithDrawRequest) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UpbitToken upbitToken = upbitJwtService.getWithDrawCoinToken(accessKey,secretKey,coinWithDrawRequest);

        HashMap<String, String> params = new HashMap<>();
        params.put("currency", coinWithDrawRequest.getCurrency());
        params.put("amount", coinWithDrawRequest.getAmount());
        params.put("address", String.valueOf(coinWithDrawRequest.getAddress()));
        params.put("secondary_address", String.valueOf(coinWithDrawRequest.getSecondary_address()));
        params.put("transaction_type", coinWithDrawRequest.getTransaction_type());

        CoinWithDrawResponse coinWithDrawResponse = upbitFeignClient.askWithdrawCoin(upbitToken.getUpbitToken(),params);
        return coinWithDrawResponse;
    }

    // 원화 출금
    public KrwWithDrawResponse askWithdrawKrw(String accessKey, String secretKey, KrwWithDrawRequest krwWithDrawRequest) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UpbitToken upbitToken = upbitJwtService.getWithDrawKrwToken(accessKey,secretKey,krwWithDrawRequest);

        HashMap<String, String> params = new HashMap<>();
        params.put("amount", krwWithDrawRequest.getAmount());
        params.put("two_factor_type", krwWithDrawRequest.getTwo_factor_type());

        KrwWithDrawResponse krwWithDrawResponse = upbitFeignClient.askWithdrawKrw(upbitToken.getUpbitToken(),params);
        return krwWithDrawResponse;
    }

    // 출금 리스트 조회
    public List<WithDraw> getWithdraws(String accessKey, String secretKey, String currency, String state, List<String> uuids, List<String> txids, Integer limit, Integer page, String orderBy) {
        UpbitToken upbitToken = upbitJwtService.getToken(accessKey,secretKey);
        List<WithDraw> withDraws = upbitFeignClient.getWithdraws(upbitToken.getUpbitToken(), currency, state, uuids, txids, limit, page, orderBy);
        return withDraws;
    }

    // 입금
    public DepositResponse getDeposit(String accessKey, String secretKey, String amount, String tow_factor_type) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UpbitToken upbitToken = upbitJwtService.getDepositToken(accessKey,secretKey,amount, tow_factor_type);

        HashMap<String, String> params = new HashMap<>();
        params.put("amount", amount);
        params.put("tow_factor_type", tow_factor_type);

        DepositResponse depositResponse = upbitFeignClient.getDeposit(upbitToken.getUpbitToken(),params);

        return depositResponse;
    }

    // 입금리스트
    public List<DepositResponse> getDepositList(String accessKey, String secretKey){
        UpbitToken upbitToken = upbitJwtService.getToken(accessKey,secretKey);
        List<DepositResponse> depositResponses = upbitFeignClient.getDepositList(upbitToken.getUpbitToken());

        return  depositResponses;
    }


}
