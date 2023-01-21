package com.example.financialfinalproject.controller;

import com.example.financialfinalproject.domain.upbit.upbitDto.TickerResponse;
import com.example.financialfinalproject.domain.upbit.upbitDto.TradeResponse;
import com.example.financialfinalproject.service.UpbitViewService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.text.ParseException;
import java.util.List;

@Controller
public class UpbitController {
    @Autowired
    UpbitViewService upbitViewService;
    @Autowired
    UpbitService upbitService;

    Model model;

    @GetMapping("/market")
    @ApiOperation(value = "마켓 코드 조회 페이지", notes = "업비트에서 거래 가능한 마켓 목록의 View를 띄운다.")
    public String getMarket(Model model, Pageable pageable, @RequestParam(defaultValue = "false") Boolean isDetails){
        List<MarketDto> markets = upbitService.getMarket(isDetails);

        //List -> Page
        final int start = (int)pageable.getOffset();
        final int end = Math.min((start + pageable.getPageSize()), markets.size());
        final Page<MarketDto> marketsPage = new PageImpl<>(markets.subList(start, end), pageable, markets.size());

        model.addAttribute("markets", marketsPage);
        model.addAttribute("previous", pageable.previousOrFirst().getPageNumber());
        model.addAttribute("next", pageable.next().getPageNumber());

        return "market";
    }

    @GetMapping("/tradeView")
    public String tradeView(@RequestParam String coin, @RequestParam Integer count, Model model) throws ParseException {
        List<TradeResponse> tradeResponses = upbitViewService.getTrade(coin, count);

        model.addAttribute("tradeList", tradeResponses);
        return "upbit/tradeTable";
    }


    @GetMapping("/tickerView")
    public String tickerView(@RequestParam String coin, Model model) {
        TickerResponse tickerResponse = upbitViewService.getTicker(coin);
        model.addAttribute("tickerInfo", tickerResponse);
        return "upbit/tickerTable";
    }
}



