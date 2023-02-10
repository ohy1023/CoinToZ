package com.example.financialfinalproject.feign;

import com.example.financialfinalproject.domain.upbit.candle.CandleDayDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleMinuteDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleMonthDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleWeekDto;
import com.example.financialfinalproject.domain.upbit.exchange.*;
import com.example.financialfinalproject.domain.upbit.quotation.MarketDto;
import com.example.financialfinalproject.domain.upbit.quotation.OrderBook;
import com.example.financialfinalproject.domain.upbit.quotation.Ticker;
import com.example.financialfinalproject.domain.upbit.quotation.Trade;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@FeignClient(name="upbit", url = "https://api.upbit.com/v1")

public interface UpbitFeignClient {

    // QUOTATION API

    @GetMapping("/market/all")
    List<MarketDto> getMarKet(@RequestParam("isDetails") Boolean isDetails);

    @GetMapping("/candles/minutes/{unit}") //unit(분 단위), 가능한 값 : [1, 3, 5, 15, 10, 30, 60, 240]
    List<CandleMinuteDto> getCandlesMinute(@PathVariable("unit") Integer unit, @RequestParam("market") String market, @RequestParam("to") String to, @RequestParam("count") String count);

    @GetMapping("candles/days")
    List<CandleDayDto> getCandlesDay(@RequestParam("market") String market, @RequestParam("to") String to, @RequestParam("count") String count, @RequestParam("convertingPriceUnit") String convertingPriceUnit);

    @GetMapping("candles/weeks")
    List<CandleWeekDto> getCandlesWeek(@RequestParam("market") String market, @RequestParam("to") String to, @RequestParam("count") String count);

    @GetMapping("candles/months")
    List<CandleMonthDto> getCandlesMonth(@RequestParam("market") String market, @RequestParam("to") String to, @RequestParam("count") String count);

    @GetMapping("/ticker") // 현재가 정보
    List<Ticker> getTicker(@RequestParam("markets") String coin);

    @GetMapping("/orderbook") // 호가 정보
    List<OrderBook> getOrderBook(@RequestParam("markets") String coin);

    @GetMapping("/trades/ticks") // 체결 정보
    List<Trade> getTrade(@RequestParam("market") String coin, @RequestParam("count") Integer count);


    // EXCHANGE API

    @GetMapping("/accounts") // 자산 - 전체 계좌 조회
    List<Acount> getAcount(@RequestHeader("Authorization") String token);

    @PostMapping("/orders") // 주문
    @ResponseBody
    OrderResponse getOrder(@RequestHeader("Authorization") String token, @RequestBody HashMap<String, String> params);


    @GetMapping("/order") // 개별주문조회
    OrderOneResponse getOrderOne(@RequestHeader("Authorization") String token, @RequestParam("uuid") String uuid);


    @DeleteMapping("/order") // 주문취소
    OrderDeleteResponse getOrderDelete(@RequestHeader("Authorization") String token, @RequestParam("uuid") String uuid);

    @GetMapping("/orders") // 주문리스트
    List<OrderResponse> getOrderList(@RequestHeader("Authorization") String token, @RequestParam("state") String state);

    @PostMapping("/deposits/krw") // 입금하기
    @ResponseBody
    DepositResponse getDeposit(@RequestHeader("Authorization") String token, @RequestBody HashMap<String, String> params);

    @GetMapping("/deposits") // 입금리스트
    List<DepositResponse> getDepositList(@RequestHeader("Authorization") String token);

    @PostMapping("/withdraws/krw") // 출금 - 원화 출금하기
    @ResponseBody
    KrwWithDrawResponse askWithdrawKrw(@RequestHeader("Authorization")String token, @RequestBody HashMap<String, String> params);


    @GetMapping("/withdraws") // 출금 - 출금 리스트 조회
    List<WithDraw> getWithdraws(@RequestHeader("Authorization") String token, @RequestParam("currency") String currency, @RequestParam("state") String state, @RequestParam("uuids") List<String> uuids, @RequestParam("txids") List<String> txids, @RequestParam("limit") Integer limit, @RequestParam("page") Integer page, @RequestParam("order_by") String orderBy);


    @PostMapping("/withdraws/coin") // 출금 - 코인 출금하기
    @ResponseBody
    CoinWithDrawResponse askWithdrawCoin(@RequestHeader("Authorization") String token, @RequestBody HashMap<String, String> params);

}