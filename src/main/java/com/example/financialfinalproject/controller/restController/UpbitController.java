package com.example.financialfinalproject.controller.restController;
import com.example.financialfinalproject.domain.Updit.Ticker;
import com.example.financialfinalproject.service.UpbitService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UpbitController {

    private final UpbitService upbitService;

    @GetMapping("/askPrice")
    public List<Ticker> getAskPrice(@RequestParam String coin){
        List<Ticker> tickers = upbitService.getOpenPrice(coin);
        return tickers; // 시가
    }

}
