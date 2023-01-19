package com.example.financialfinalproject.feign;

import com.example.financialfinalproject.domain.upbit.dto.CandleDayDto;
import com.example.financialfinalproject.domain.upbit.dto.CandleMinuteDto;
import com.example.financialfinalproject.domain.upbit.dto.CandleWeekDto;
import com.example.financialfinalproject.domain.upbit.dto.MarketDto;
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
    List<CandleMinuteDto> getCandlesMinute(@PathVariable Integer unit, @RequestParam String market, @RequestParam String to, @RequestParam String count);

    @GetMapping("candles/days")
    List<CandleDayDto> getCandlesDay(@RequestParam String market, @RequestParam String to, @RequestParam String count, @RequestParam String convertingPriceUnit);

    @GetMapping("candles/weeks")
    List<CandleWeekDto> getCandlesWeek(@RequestParam String market, @RequestParam String to, @RequestParam String count);
}
