package com.example.financialfinalproject.feign;
import com.example.financialfinalproject.domain.upbit.OrderBook;
import com.example.financialfinalproject.domain.upbit.Ticker;
import com.example.financialfinalproject.domain.upbit.Trade;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="upbit", url = "https://api.upbit.com/v1")
public interface UpbitFeignClient {

    @GetMapping("/ticker") // 현재가 정보
    List<Ticker> getTicker(@RequestParam("markets") String coin);

    @GetMapping("/orderbook") // 호가 정보
    List<OrderBook> getOrderBook(@RequestParam("markets") String coin);

    @GetMapping("/trades/ticks") // 체결 정보
    List<Trade> getTrade(@RequestParam("market") String coin, @RequestParam("count") Integer count);

}
