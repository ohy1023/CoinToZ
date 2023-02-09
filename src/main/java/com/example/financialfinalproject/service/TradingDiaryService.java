package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.dto.TradingDiaryDto;
import com.example.financialfinalproject.domain.dto.TradingDiaryListDto;
import com.example.financialfinalproject.domain.entity.TradingDiary;
import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.domain.response.MyCoinCntResponse;
import com.example.financialfinalproject.domain.upbit.exchange.OrderDeleteResponse;
import com.example.financialfinalproject.domain.upbit.exchange.OrderResponse;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.exception.ErrorCode;
import com.example.financialfinalproject.repository.TradingDiaryRepository;
import com.example.financialfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.example.financialfinalproject.exception.ErrorCode.EMAIL_NOT_FOUND;
import static com.example.financialfinalproject.exception.ErrorCode.USERNAME_NOT_FOUND;

@RequiredArgsConstructor
@Service
@Slf4j
public class TradingDiaryService {

    private final TradingDiaryRepository tradingDiaryRepository;
    private final UserRepository userRepository;

    private String BitCoin = "KRW-BTC";
    private String Ethereum = "KRW-ETH";
    private String Ripple = "KRW-XRP";
    private String ADA = "KRW-ADA";
    private String DogeCoin = "KRW-DOGE";


    public List<TradingDiaryListDto> listOf(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
                });
        return tradingDiaryRepository.findAllByUserOrderByDate(user)
                .stream().map(TradingDiaryListDto::toDto).collect(Collectors.toList());

    }

    public MyCoinCntResponse getCoins(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
                });
        double sumAllCnt = (tradingDiaryRepository.findSumVolumeByUser(user)) == null ? 0 : tradingDiaryRepository.findSumVolumeByUser(user);
        double bitCoinCnt = (tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(BitCoin, user)) == null ? 0 : tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(BitCoin, user);
        double ethereumCnt = (tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(Ethereum, user)) == null ? 0 : tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(Ethereum, user);
        double rippleCnt = (tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(Ripple, user)) == null ? 0 : tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(Ripple, user);
        double ADACnt = (tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(ADA, user)) == null ? 0 : tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(ADA, user);
        double dogeCoinCnt = (tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(DogeCoin, user)) == null ? 0 : tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(DogeCoin, user);
        double etcCnt = (sumAllCnt - (bitCoinCnt + ethereumCnt + rippleCnt + ADACnt + dogeCoinCnt));

        return MyCoinCntResponse.builder()
                .bitCoin(bitCoinCnt)
                .ethereum(ethereumCnt)
                .ADA(ADACnt)
                .dogeCoin(dogeCoinCnt)
                .ripple(rippleCnt)
                .etc(etcCnt)
                .build();

    }

    public List<TradingDiaryListDto> findListByCond(String email, String startDate, String endDate) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
                });
        log.info("{}", LocalDateTime.parse(startDate));
        log.info("{}", LocalDateTime.parse(endDate));

        return tradingDiaryRepository.searchDateRange(user, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate))
                .stream().map(TradingDiaryListDto::toDto).collect(Collectors.toList());

    }

    public Double avgRevenueByTime(String email, int startTime, int endTime) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
                });

        LocalDateTime today = LocalDateTime.now();

        LocalDateTime start = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), startTime, 0);
        LocalDateTime end = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), endTime, 0);

        return (tradingDiaryRepository.findAvgRevenue(user, start, end)) == null ? 0 : (tradingDiaryRepository.findAvgRevenue(user, start, end));
    }

    public void write(OrderResponse orderResponse, User user) {

        userRepository.findById(user.getId()).orElseThrow(()-> new AppException(USERNAME_NOT_FOUND, "해당 user 를 찾을 수 없습니다."));

        // 수수료 반올림
        int fee = (int) Math.round(orderResponse.getPaid_fee());


        // 매수
        if (orderResponse.getSide().equals("bid")) {

            TradingDiary tradingDiary = TradingDiary.builder()
                    .bid_created_at(LocalDateTime.parse(orderResponse.getCreated_at().split("\\+")[0])) // 매수 주문시간
                    .market(orderResponse.getMarket())
                    .bid_price(orderResponse.getPrice() + fee) // 매수가격(수수료 포함)
                    .volume(Double.valueOf(orderResponse.getExecuted_volume())) // 수량
                    .bidUuid(orderResponse.getUuid())
                    .user(user)
                    .build();

            tradingDiaryRepository.save(tradingDiary);
        }

        // 매도
        else if (orderResponse.getSide().equals("ask")) {

            TradingDiary tradingDiarys = tradingDiaryRepository.findByMarket(orderResponse.getMarket());
            tradingDiarys.setAsk_created_at(LocalDateTime.parse(orderResponse.getCreated_at().split("\\+")[0])); // 매도시간
            tradingDiarys.setAsk_price(orderResponse.getPrice() + fee); // 매도가격(수수료 포함)
            tradingDiarys.setAskUuid(orderResponse.getUuid());
            tradingDiarys.setVolume(Double.valueOf(orderResponse.getExecuted_volume()));

            tradingDiarys.setArbitrage(tradingDiarys.getAsk_price() - tradingDiarys.getBid_price()); // 차익 (매수가격 - 매도가격)

            double revenue = ((double) tradingDiarys.getBid_price() / (double) tradingDiarys.getArbitrage()); // 수익률 계산
            tradingDiarys.setRevenue(Math.round(revenue * 100) / 100.0); // 수익률 (소숫점 2자리 까지 표현)

            tradingDiaryRepository.save(tradingDiarys);
        }
    }

    public TradingDiaryDto edit(Long id, String comment) {

        Optional<TradingDiary> tradingDiary = tradingDiaryRepository.findById(id);
        tradingDiary.orElseThrow(() -> new AppException(ErrorCode.DIARY_NOT_FOUND, "해당 일지가 없습니다."));

        TradingDiary tradingDiaryEdit = tradingDiary.get();

        tradingDiaryEdit.setComment(comment);

        tradingDiaryRepository.save(tradingDiaryEdit);


        return TradingDiaryDto.builder()
                .message("메모가 작성되었습니다.")
                .build();
    }


    public TradingDiaryDto delete(Long id) {

        Optional<TradingDiary> tradingDiary = tradingDiaryRepository.findById(id);
        tradingDiary.orElseThrow(() -> new AppException(ErrorCode.DIARY_NOT_FOUND, "해당 일지가 없습니다."));

        tradingDiaryRepository.deleteById(id);

        return TradingDiaryDto.builder()
                .message("일지가 삭제되었습니다.")
                .build();
    }

    //주문취소
    public TradingDiaryDto orderDelete(OrderDeleteResponse orderDeleteResponse){

        if (orderDeleteResponse.getSide().equals("bid")) {
            tradingDiaryRepository.deleteByBidUuid(orderDeleteResponse.getUuid());
        }

       else if (orderDeleteResponse.getSide().equals("ask")) {
            tradingDiaryRepository.deleteByAskUuid(orderDeleteResponse.getUuid());
        }

    return  TradingDiaryDto.builder()
            .message("주문이 취소되어 일지가 삭제되었습니다.")
            .build();
    }

}






