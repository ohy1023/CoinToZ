package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.Updit.Ticker;
import com.example.financialfinalproject.feign.UpbitFeignClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
public class UpbitService {

    private final UpbitFeignClient upbitFeignClient;

    public List<Ticker> getOpenPrice (String coin){
    return upbitFeignClient.getAskPrice("KRW-"+ coin.toUpperCase()); //toUpperCase = 대문자 반환 메소드
    }

}
