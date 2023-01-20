package com.example.financialfinalproject.controller.restController;

import com.example.financialfinalproject.domain.upbit.OrderBook;
import com.example.financialfinalproject.domain.upbit.Ticker;
import com.example.financialfinalproject.domain.upbit.Trade;
import com.example.financialfinalproject.service.UpbitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UpbitRestController {

    private final UpbitService upbitService;

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
    }





