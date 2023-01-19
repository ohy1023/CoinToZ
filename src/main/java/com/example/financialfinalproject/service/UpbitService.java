package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.upbit.dto.CandleMinuteDto;
import com.example.financialfinalproject.domain.upbit.dto.MarketDto;
import com.example.financialfinalproject.feign.UpbitFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class UpbitService {

    private final UpbitFeignClient upbitFeignClient;

    public List<MarketDto> getMarket(Boolean isDetails) {
        return upbitFeignClient.getMarKet(isDetails);
    }

    public List<CandleMinuteDto> getCandlesMinute(Integer unit, String market, String to, String count) {
        return upbitFeignClient.getCandlesMinute(unit,market,to,count);
    }
}
