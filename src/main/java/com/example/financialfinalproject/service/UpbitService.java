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
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.feign.UpbitFeignClient;
import com.example.financialfinalproject.global.jwt.service.UpbitJwtService;
import com.example.financialfinalproject.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.example.financialfinalproject.exception.ErrorCode.EMAIL_NOT_FOUND;

@RequiredArgsConstructor
@Getter
@Service
@Slf4j
public class UpbitService {

    private final UpbitFeignClient upbitFeignClient;
    private final UpbitJwtService upbitJwtService;

    private final TradingDiaryService tradingDiaryService;

    private final UserRepository userRepository;


    // QUOTATION API


    @Transactional
    public List<MarketDto> getMarket(Boolean isDetails) {
        return upbitFeignClient.getMarKet(isDetails);
    }

    @Transactional
    public List<CandleMinuteDto> getCandlesMinute(Integer unit, String market, String to, String count) {
        return upbitFeignClient.getCandlesMinute(unit, market, to, count);
    }

    @Transactional
    public List<CandleDayDto> getCandlesDay(String market, String to, String count, String convertingPriceUnit) {
        return upbitFeignClient.getCandlesDay(market, to, count, convertingPriceUnit);
    }

    @Transactional
    public List<CandleWeekDto> getCandlesWeek(String market, String to, String count) {
        return upbitFeignClient.getCandlesWeek(market, to, count);
    }

    @Transactional
    public List<CandleMonthDto> getCandlesMonth(String market, String to, String count) {
        return upbitFeignClient.getCandlesMonth(market, to, count);
    }


    @Transactional
    public Ticker getTicker(String coin) { // 현재가 정보 - 1줄
        List<Ticker> tickerList = upbitFeignClient.getTicker("KRW-" + coin.toUpperCase());
        return tickerList.get(0);
    }

    @Transactional
    public OrderBook getOrderBook(String coin) { // 호가 정보 - 리스트
        List<OrderBook> orderBookList = upbitFeignClient.getOrderBook("KRW-" + coin.toUpperCase());
        return orderBookList.get(0);
    }

    @Transactional
    public List<Trade> getTrade(String coin, Integer count) { // 체결 정보 - 리스트
        List<Trade> tradeList = upbitFeignClient.getTrade("KRW-" + coin.toUpperCase(), count);
        return tradeList;
    }


    // EXCHANGE API


    // 계좌조회

    @Transactional
    public List<Acount> getAcount(String accessKey, String secretKey) { // 전체계좌조회
        UpbitToken upbitToken = upbitJwtService.getToken(accessKey, secretKey);
        List<Acount> acounts = upbitFeignClient.getAcount(upbitToken.getUpbitToken());
        return acounts;
    }


    // 주문
    @Transactional
    public OrderResponse getOrder(String accessKey, String secretKey, OrderRequest orderRequest, User user) throws UnsupportedEncodingException, NoSuchAlgorithmException, JsonProcessingException, InterruptedException {
        UpbitToken upbitToken = upbitJwtService.getOrderToken(accessKey, secretKey, orderRequest);

        HashMap<String, String> params = new HashMap<>();
        params.put("market", orderRequest.getMarket());
        params.put("side", orderRequest.getSide());
        params.put("volume", orderRequest.getVolume());
        params.put("price", orderRequest.getPrice());
        params.put("ord_type", orderRequest.getOrd_type());


        OrderResponse orderResponse = upbitFeignClient.getOrder(upbitToken.getUpbitToken(), params); // uuid 추출
        UpbitToken orderOneToken = upbitJwtService.getOrderDeleteToken(accessKey, secretKey, orderResponse.getUuid());
        TimeUnit.SECONDS.sleep(1);
        OrderOneResponse orderOneResponse = upbitFeignClient.getOrderOne(orderOneToken.getUpbitToken(), orderResponse.getUuid());
        tradingDiaryService.write(orderOneResponse, user, orderResponse);

        return orderResponse;
    }

    //개별주문조회
    @Transactional
    public OrderOneResponse getOrderOne(String accessKey, String secretKey, String uuid) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UpbitToken upbitToken = upbitJwtService.getOrderDeleteToken(accessKey, secretKey, uuid); // 삭제랑 개별조회랑 토큰이 같아서 같이 사용
        OrderOneResponse orderOneResponse = upbitFeignClient.getOrderOne(upbitToken.getUpbitToken(), uuid);
        return orderOneResponse;
    }


    // 주문리스트
    @Transactional
    public List<OrderResponse> getOrderList(String accessKey, String secretKey, String state, User user) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UpbitToken upbitToken = upbitJwtService.getOrderListToken(accessKey, secretKey, state);
        List<OrderResponse> orderResponses = upbitFeignClient.getOrderList(upbitToken.getUpbitToken(), state);
        //tradingDiaryService.write(orderResponses.get(0),user);
        return orderResponses;
    }


    // 주문취소
    @Transactional
    public OrderDeleteResponse getOrderDelete(String accessKey, String secretKey, String uuid, String email) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UpbitToken upbitToken = upbitJwtService.getOrderDeleteToken(accessKey, secretKey, uuid);
        OrderDeleteResponse orderResponse = upbitFeignClient.getOrderDelete(upbitToken.getUpbitToken(), uuid);
        tradingDiaryService.orderDelete(email, orderResponse);

        return orderResponse;
    }


    // 코인 출금하기
    @Transactional
    public CoinWithDrawResponse askWithdrawCoin(String accessKey, String secretKey, CoinWithDrawRequest coinWithDrawRequest) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UpbitToken upbitToken = upbitJwtService.getWithDrawCoinToken(accessKey, secretKey, coinWithDrawRequest);

        HashMap<String, String> params = new HashMap<>();
        params.put("currency", coinWithDrawRequest.getCurrency());
        params.put("amount", coinWithDrawRequest.getAmount());
        params.put("address", String.valueOf(coinWithDrawRequest.getAddress()));
        params.put("secondary_address", String.valueOf(coinWithDrawRequest.getSecondary_address()));
        params.put("transaction_type", coinWithDrawRequest.getTransaction_type());

        CoinWithDrawResponse coinWithDrawResponse = upbitFeignClient.askWithdrawCoin(upbitToken.getUpbitToken(), params);
        return coinWithDrawResponse;
    }

    // 원화 출금
    @Transactional
    public KrwWithDrawResponse askWithdrawKrw(String accessKey, String secretKey, KrwWithDrawRequest krwWithDrawRequest) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UpbitToken upbitToken = upbitJwtService.getWithDrawKrwToken(accessKey, secretKey, krwWithDrawRequest);

        HashMap<String, String> params = new HashMap<>();
        params.put("amount", krwWithDrawRequest.getAmount());
        params.put("two_factor_type", krwWithDrawRequest.getTwo_factor_type());

        KrwWithDrawResponse krwWithDrawResponse = upbitFeignClient.askWithdrawKrw(upbitToken.getUpbitToken(), params);
        return krwWithDrawResponse;
    }

    // 출금 리스트 조회
    @Transactional
    public List<WithDraw> getWithdraws(String accessKey, String secretKey, String currency, String state, List<String> uuids, List<String> txids, Integer limit, Integer page, String orderBy) {
        UpbitToken upbitToken = upbitJwtService.getToken(accessKey, secretKey);
        List<WithDraw> withDraws = upbitFeignClient.getWithdraws(upbitToken.getUpbitToken(), currency, state, uuids, txids, limit, page, orderBy);
        return withDraws;
    }

    // 입금
    @Transactional
    public DepositResponse getDeposit(String accessKey, String secretKey, String amount, String two_factor_type) throws UnsupportedEncodingException, NoSuchAlgorithmException {
        UpbitToken upbitToken = upbitJwtService.getDepositToken(accessKey, secretKey, amount, two_factor_type);

        HashMap<String, String> params = new HashMap<>();
        params.put("amount", amount);
        params.put("two_factor_type", two_factor_type);

        DepositResponse depositResponse = upbitFeignClient.getDeposit(upbitToken.getUpbitToken(), params);

        return depositResponse;
    }

    // 입금리스트
    @Transactional
    public List<DepositResponse> getDepositList(String accessKey, String secretKey) {
        UpbitToken upbitToken = upbitJwtService.getToken(accessKey, secretKey);
        List<DepositResponse> depositResponses = upbitFeignClient.getDepositList(upbitToken.getUpbitToken());

        return depositResponses;
    }

    //수익률 계산
    @Transactional
    public Double avgRevenueCalculation(String email) {
        double purchaseAmount = 0.0;
        double evaluationAmount = 0.0;

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
                });

        List<Acount> acounts = getAcount(user.getAccessKey(), user.getSecretKey());

        log.info("Accout Size : {}", acounts.size());

        for (Acount acount : acounts) {
            if (!acount.getCurrency().equals("KRW")) {
                purchaseAmount += (Double.parseDouble(acount.getAvg_buy_price()) * Double.parseDouble(acount.getBalance()));
                Ticker ticker = getTicker(acount.getCurrency());
                evaluationAmount += (ticker.getTrade_price() * Double.parseDouble(acount.getBalance()));
            }
        }
        log.info("총 매수 금액 : {}", purchaseAmount);
        log.info("총 평가 금액 : {}", evaluationAmount);

        double profitAndLossStatement = evaluationAmount - purchaseAmount;

        log.info("평가 손익 : {}", profitAndLossStatement);

        log.info("수익률 : {}", profitAndLossStatement / purchaseAmount);

        return Math.round((profitAndLossStatement / purchaseAmount) * 10000) / 100.0;
    }

}
