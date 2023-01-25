package com.example.financialfinalproject.controller;

import com.example.financialfinalproject.domain.upbit.TickerResponse;
import com.example.financialfinalproject.domain.upbit.TradeResponse;
import com.example.financialfinalproject.service.UpbitViewService;
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

    Model model;

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



