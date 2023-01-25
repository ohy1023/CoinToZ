package com.example.financialfinalproject.controller.restController;

import com.example.financialfinalproject.domain.upbit.*;
import com.example.financialfinalproject.domain.upbit.candle.CandleDayDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleMinuteDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleMonthDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleWeekDto;
import com.example.financialfinalproject.service.UpbitService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UpbitRestController {

    private final UpbitService upbitService;

    @GetMapping("/getMarket")
    @ApiOperation(value = "마켓 코드 조회", notes = "업비트에서 거래 가능한 마켓 목록")
    public List<MarketDto> getMarket(@RequestParam(defaultValue = "false") Boolean isDetails){
        List<MarketDto> markets = upbitService.getMarket(isDetails);
        return markets;
    }

    @GetMapping("/getCandlesMinute/{unit}")
    @ApiOperation(value = "분(Minute) 캔들 조회", notes = "시세 분(Minute) 캔들 조회")
    public List<CandleMinuteDto> getCandlesMinute(@PathVariable("unit") Integer unit, @RequestParam(value = "market", required = true) String market, @RequestParam(value = "to", required = false) String to, @RequestParam(value = "count", required = false) String count) {
        List<CandleMinuteDto> minuteCandles = upbitService.getCandlesMinute(unit, market, to, count);
        return minuteCandles;
    }

    @GetMapping("/getCandlesDay")
    @ApiOperation(value = "일(Day) 캔들 조회", notes = "시세 일(Day) 캔들 조회")
    public List<CandleDayDto> getCandlesDay(@RequestParam(value = "market", required = true) String market, @RequestParam(value = "to", required = false) String to, @RequestParam(value = "count", required = false) String count, @RequestParam(value = "convertingPriceUnit", required = false) String convertingPriceUnit) {
        List<CandleDayDto> dayCandles = upbitService.getCandlesDay(market, to, count, convertingPriceUnit);
        return dayCandles;
    }

    @GetMapping("/getCandlesWeek")
    @ApiOperation(value = "주(Week) 캔들 조회", notes = "시세 주(Week) 캔들 조회")
    public List<CandleWeekDto> getCandlesWeek(@RequestParam(value = "market", required = true) String market, @RequestParam(value = "to", required = false) String to, @RequestParam(value = "count", required = false) String count) {
        List<CandleWeekDto> weekCandles = upbitService.getCandlesWeek(market, to, count);
        return weekCandles;
    }

    @GetMapping("/getCandlesMonth")
    @ApiOperation(value = "월(Month) 캔들 조회", notes = "시세 월(Month) 캔들 조회")
    public List<CandleMonthDto> getCandlesMonth(@RequestParam(value = "market", required = true) String market, @RequestParam(value = "to", required = false) String to, @RequestParam(value = "count", required = false) String count) {
        List<CandleMonthDto> monthCandles = upbitService.getCandlesMonth(market, to, count);
        return monthCandles;
    }

    @GetMapping("/ticker") // 현재가 정보
    public Ticker getTicker(@RequestParam String coin){
        Ticker tickerList = upbitService.getTicker(coin);
        return tickerList;

    }

    @GetMapping("/orderbook")
    public OrderBook getOrderBook(@RequestParam String coin){
        OrderBook orderbookUnitsList = upbitService.getOrderBook(coin);
        return orderbookUnitsList;
    }


    @GetMapping("/trade")
    public List<Trade> getTrade(@RequestParam String coin, @RequestParam Integer count){
        List<Trade> tradeResponse = upbitService.getTrade(coin, count);
        return tradeResponse;
    }


    @GetMapping("/token")
    public UpbitToken getToken(String accesskey, String secretKey){
        UpbitToken upbitToken = upbitService.getToken(accesskey,secretKey);
        return upbitToken;
    }


    @GetMapping("/acount")
    public List<Acount> getAcount(@RequestParam String token){
        List<Acount> acounts = upbitService.getAcount(token);
        return acounts;
    }


}





