package com.example.financialfinalproject.feign;
import com.example.financialfinalproject.domain.upbit.OrderBook;
import com.example.financialfinalproject.domain.upbit.Ticker;
import com.example.financialfinalproject.domain.upbit.Trade;

import com.example.financialfinalproject.domain.upbit.dto.*;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="upbit", url = "https://api.upbit.com/v1")
public interface UpbitFeignClient {

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


}