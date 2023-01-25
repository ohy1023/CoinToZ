package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.upbit.Ticker;
import com.example.financialfinalproject.domain.upbit.Trade;
import com.example.financialfinalproject.domain.upbit.TickerResponse;
import com.example.financialfinalproject.domain.upbit.TradeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class UpbitViewService { // UpbitController

    private final UpbitService upbitService;

    public List<TradeResponse> getTrade(String coin, Integer count) { // 실시간 체결표

        List<Trade> tradeList = upbitService.getTrade(coin, count);
        Ticker ticker = upbitService.getTicker(coin);

        // 시간 모양 바꾸기
        String Kst = ticker.getTrade_date_kst() + ticker.getTrade_time_kst();
        StringBuffer buf = new StringBuffer(Kst);
        buf.insert(4, "-");
        buf.insert(7, "-");
        buf.insert(10, " ");
        buf.insert(13, ":");
        buf.insert(16, ":");

        String date = buf.toString();

        // 매수 or 매도
        for (int i = 0; i < tradeList.size(); i++) {

            if (tradeList.get(i).getAsk_bid().equals("BID")) tradeList.get(i).setAsk_bid("매수");
            if (tradeList.get(i).getAsk_bid().equals("ASK")) tradeList.get(i).setAsk_bid("매도");

        }

        List<TradeResponse> tradeResponses = tradeList.stream().map(tradeLists -> TradeResponse.builder()
                .tradeTime(date)
                .ask_bid(tradeLists.getAsk_bid())
                .CoinPrice(tradeLists.getTrade_price()) // 체결가격 = (코인가격)
                .tradePrice(String.format("%.0f", tradeLists.getTrade_price() * tradeLists.getTrade_volume())) // 체결금액
                .tradeVolume(tradeLists.getTrade_volume())
                .build()).collect(Collectors.toList());

        return tradeResponses;
    }

    public TickerResponse getTicker(String coin) { // 실시간 코인표
        Ticker ticker = upbitService.getTicker(coin);

        // 상태 변경
        if (ticker.getChange().equals("EVEN")) ticker.setChange("보합");
        if (ticker.getChange().equals("RISE")) ticker.setChange("상승");
        if (ticker.getChange().equals("FALL")) ticker.setChange("하락");

            //지수 제거
       double gap = ticker.getTrade_price() - ticker.getPrev_closing_price();

       BigDecimal bigDecimal = new BigDecimal(ticker.getTrade_price());
       BigDecimal bigDecimal2 = new BigDecimal(ticker.getPrev_closing_price());
       BigDecimal bigDecimal3 = new BigDecimal(ticker.getHigh_price());
       BigDecimal bigDecimal4 = new BigDecimal(ticker.getLow_price());
       BigDecimal bigDecimal5 = new BigDecimal(gap);

       String tradePrice = bigDecimal.toPlainString();
       String prevPrice = bigDecimal2.toPlainString();
       String highPrice = bigDecimal3.toPlainString();
       String lowPrice = bigDecimal4.toPlainString();
       String gapPrice = bigDecimal5.toPlainString();


            TickerResponse tickerResponse = TickerResponse.builder()
                    .change(ticker.getChange())
                    .trade_price(tradePrice)
                    .gap_price(gapPrice)
                    .prev_closing_price(prevPrice)
                    .high_price(highPrice)
                    .low_price(lowPrice)
                    .build();

            // 전일대비 부호 표시
            if (tickerResponse.getChange().equals("상승"))
                tickerResponse.setGap_price("+" + tickerResponse.getGap_price());
            if (tickerResponse.getChange().equals("하락"))
                tickerResponse.setGap_price("-" + tickerResponse.getGap_price());
            if (tickerResponse.getChange().equals("보합"))
                tickerResponse.setGap_price("" + tickerResponse.getGap_price());

            return tickerResponse;
        }

    }




