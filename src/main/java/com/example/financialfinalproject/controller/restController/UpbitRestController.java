package com.example.financialfinalproject.controller.restController;

import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.domain.upbit.candle.CandleDayDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleMinuteDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleMonthDto;
import com.example.financialfinalproject.domain.upbit.candle.CandleWeekDto;
import com.example.financialfinalproject.domain.upbit.exchange.*;
import com.example.financialfinalproject.domain.upbit.quotation.MarketDto;
import com.example.financialfinalproject.domain.upbit.quotation.OrderBook;
import com.example.financialfinalproject.domain.upbit.quotation.Ticker;
import com.example.financialfinalproject.domain.upbit.quotation.Trade;
import com.example.financialfinalproject.global.jwt.service.UpbitJwtService;
import com.example.financialfinalproject.repository.UserRepository;
import com.example.financialfinalproject.service.UpbitService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.io.UnsupportedEncodingException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Slf4j
@RestController
public class UpbitRestController {

    private final UpbitService upbitService;

    private final UpbitJwtService upbitJwtService;
    private final UserRepository userRepository;

    // QUOTATION API

    @GetMapping("/getMarket")
    @ApiOperation(value = "마켓 코드 조회", notes = "업비트에서 거래 가능한 마켓 목록")
    public List<MarketDto> getMarket(@RequestParam(defaultValue = "false") Boolean isDetails){
        List<MarketDto> markets = upbitService.getMarket(isDetails);
        return markets;
    }

    @GetMapping("/getCandlesMinute/{unit}")
    @ApiOperation(value = "분(Minute) 캔들 조회", notes = "시세 분(Minute) 캔들 조회")
    public List<CandleMinuteDto> getCandlesMinute(@PathVariable("unit") Integer unit, @RequestParam(value = "market", required = true) String market, @RequestParam(value = "to", required = false) String to, @RequestParam(value = "count", required = false) String count) {
        List<CandleMinuteDto> minuteCandles = upbitService.getCandlesMinute(unit, market, to, count);
        return minuteCandles;
    }

    @GetMapping("/getCandlesDay")
    @ApiOperation(value = "일(Day) 캔들 조회", notes = "시세 일(Day) 캔들 조회")
    public List<CandleDayDto> getCandlesDay(@RequestParam(value = "market", required = true) String market, @RequestParam(value = "to", required = false) String to, @RequestParam(value = "count", required = false) String count, @RequestParam(value = "convertingPriceUnit", required = false) String convertingPriceUnit) {
        List<CandleDayDto> dayCandles = upbitService.getCandlesDay(market, to, count, convertingPriceUnit);
        return dayCandles;
    }

    @GetMapping("/getCandlesWeek")
    @ApiOperation(value = "주(Week) 캔들 조회", notes = "시세 주(Week) 캔들 조회")
    public List<CandleWeekDto> getCandlesWeek(@RequestParam(value = "market", required = true) String market, @RequestParam(value = "to", required = false) String to, @RequestParam(value = "count", required = false) String count) {
        List<CandleWeekDto> weekCandles = upbitService.getCandlesWeek(market, to, count);
        return weekCandles;
    }

    @GetMapping("/getCandlesMonth")
    @ApiOperation(value = "월(Month) 캔들 조회", notes = "시세 월(Month) 캔들 조회")
    public List<CandleMonthDto> getCandlesMonth(@RequestParam(value = "market", required = true) String market, @RequestParam(value = "to", required = false) String to, @RequestParam(value = "count", required = false) String count) {
        List<CandleMonthDto> monthCandles = upbitService.getCandlesMonth(market, to, count);
        return monthCandles;
    }

    @GetMapping("/ticker") // 현재가 정보
    @ApiOperation(value = "현재가 정보", notes = "현재가 정보")
    public Ticker getTicker(@RequestParam String coin){
        Ticker tickerList = upbitService.getTicker(coin);
        return tickerList;

    }

    @GetMapping("/orderbook")
    @ApiOperation(value = "호가 정보", notes = "호가 정보")
    public OrderBook getOrderBook(@RequestParam String coin){
        OrderBook orderbookUnitsList = upbitService.getOrderBook(coin);
        return orderbookUnitsList;
    }


    @GetMapping("/trade")
    @ApiOperation(value = "체결 정보", notes = "체결 정보")
    public List<Trade> getTrade(@RequestParam String coin, @RequestParam Integer count){
        List<Trade> tradeResponse = upbitService.getTrade(coin, count);
        return tradeResponse;
    }


    // EXCHANGE API

    @GetMapping("/acount") //전체 계좌조회
    @ApiOperation(value = "전체계좌조회", notes = "전체계좌조회")
    public List<Acount> getAcount(@ApiIgnore Authentication authentication){

        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String accessKey = user.get().getAccessKey();
        String secretKey = user.get().getSecretKey();

        List<Acount> acounts = upbitService.getAcount(accessKey,secretKey);
        return acounts;
    }

    @PostMapping("/order") // 주문하기
    @ApiOperation(value = "주문하기", notes = "주문하기")
    public OrderResponse getOrder(@ApiIgnore Authentication authentication, @RequestBody OrderRequest orderRequest) throws UnsupportedEncodingException, NoSuchAlgorithmException, JsonProcessingException {

        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String accessKey = user.get().getAccessKey();
        String secretKey = user.get().getSecretKey();

        OrderResponse response = upbitService.getOrder(accessKey,secretKey,orderRequest,user.get());
        return response;
    }

    @DeleteMapping // 주문취소
    @ApiOperation(value = "주문취소", notes = "주문취소")
    public OrderDeleteResponse getOrderDelete(@ApiIgnore Authentication authentication, @RequestParam String uuid) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String accessKey = user.get().getAccessKey();
        String secretKey = user.get().getSecretKey();

        OrderDeleteResponse orderDeleteResponse = upbitService.getOrderDelete(accessKey,secretKey,uuid, email);
        return orderDeleteResponse;
    }

    @GetMapping("/orders") // 주문리스트
    @ApiOperation(value = "주문리스트", notes = "주문리스트")
    public List<OrderResponse> getOrderList(@ApiIgnore Authentication authentication, @RequestParam String state) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String accessKey = user.get().getAccessKey();
        String secretKey = user.get().getSecretKey();

        List<OrderResponse> orderResponses = upbitService.getOrderList(accessKey,secretKey, state, user.get());



        return orderResponses;
    }

    @PostMapping("/deposit") // 입금하기
    @ApiOperation(value = "입금하기", notes = "입금하기")
    public DepositResponse getDeposit(@ApiIgnore Authentication authentication, @RequestParam String amount, @RequestParam String two_factor_type) throws UnsupportedEncodingException, NoSuchAlgorithmException {

        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String accessKey = user.get().getAccessKey();
        String secretKey = user.get().getSecretKey();

        DepositResponse depositResponse = upbitService.getDeposit(accessKey,secretKey,amount,two_factor_type);
        return depositResponse;
    }

    @GetMapping("/deposits") // 입금리스트
    @ApiOperation(value = "입금리스트", notes = "입금리스트")
    public List<DepositResponse> getDepositList(@ApiIgnore Authentication authentication){

        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String accessKey = user.get().getAccessKey();
        String secretKey = user.get().getSecretKey();

        List<DepositResponse> depositResponses = upbitService.getDepositList(accessKey,secretKey);
        return depositResponses;
    }


    @PostMapping("/withdraws/krw")
    @ApiOperation(value = "출금하기", notes = "출금하기")
    public KrwWithDrawResponse askWithdrawKrw(@ApiIgnore Authentication authentication, @RequestBody KrwWithDrawRequest krwWithDrawRequest) throws UnsupportedEncodingException, NoSuchAlgorithmException, JsonProcessingException {

        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String accessKey = user.get().getAccessKey();
        String secretKey = user.get().getSecretKey();

        KrwWithDrawResponse response = upbitService.askWithdrawKrw(accessKey, secretKey, krwWithDrawRequest);
        return response;

    }

    @PostMapping("/withdraws/coin")
    @ApiOperation(value = "코인출금", notes = "코인출금")
    public CoinWithDrawResponse askWithdrawCoin(@ApiIgnore Authentication authentication, @RequestBody CoinWithDrawRequest coinWithDrawRequest) throws UnsupportedEncodingException, NoSuchAlgorithmException, JsonProcessingException {

        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String accessKey = user.get().getAccessKey();
        String secretKey = user.get().getSecretKey();

        CoinWithDrawResponse response = upbitService.askWithdrawCoin(accessKey,secretKey,coinWithDrawRequest);
        return response;
    }


    @GetMapping("/withdraws")
    @ApiOperation(value = "출금리스트", notes = "출금리스트")
    public List<WithDraw> getWithdraws(@ApiIgnore Authentication authentication, @RequestParam("currency") String currency, @RequestParam("state") String state, @RequestParam("uuids") List<String> uuids, @RequestParam("txids") List<String> txids, @RequestParam(value = "limit", defaultValue = "100") Integer limit, @RequestParam(value = "page", defaultValue = "1") Integer page, @RequestParam(value = "order_by", defaultValue = "desc") String orderBy){

        String email = authentication.getName();
        Optional<User> user = userRepository.findByEmail(email);
        String accessKey = user.get().getAccessKey();
        String secretKey = user.get().getSecretKey();

        List<WithDraw> withDraws = upbitService.getWithdraws(accessKey,secretKey,currency, state, uuids, txids, limit, page, orderBy);
        return withDraws;
    }


}





