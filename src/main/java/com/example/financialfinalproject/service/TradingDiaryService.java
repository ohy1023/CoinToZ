package com.example.financialfinalproject.service;

import com.example.financialfinalproject.controller.restController.UpbitRestController;
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

import static com.example.financialfinalproject.exception.ErrorCode.*;

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


//    public List<TradingDiaryListDto> listOf(String email) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> {
//                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
//                });
//        return tradingDiaryRepository.findAllByUserOrderByDate(user)
//                .stream().map(TradingDiaryListDto::toDto).collect(Collectors.toList());
//
//    }

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

//    public List<TradingDiaryListDto> findListByCond(String email, String startDate, String endDate) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> {
//                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
//                });
//        log.info("{}", LocalDateTime.parse(startDate));
//        log.info("{}", LocalDateTime.parse(endDate));
//
//        return tradingDiaryRepository.searchDateRange(user, LocalDateTime.parse(startDate), LocalDateTime.parse(endDate))
//                .stream().map(TradingDiaryListDto::toDto).collect(Collectors.toList());
//
//    }

//    public Double avgRevenueByTime(String email, int startTime, int endTime) {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> {
//                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
//                });
//
//        LocalDateTime today = LocalDateTime.now();
//
//        LocalDateTime start = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), startTime, 0,0);
//        LocalDateTime end = LocalDateTime.of(today.getYear(), today.getMonth(), today.getDayOfMonth(), endTime, 59, 59);
//
//        return (tradingDiaryRepository.findAvgRevenue(user, start, end)) == null ? 0 : (tradingDiaryRepository.findAvgRevenue(user, start, end));
//    }

    public void write(OrderResponse orderResponse, User user, OrderResponse response, OrderResponse ask) {


        userRepository.findById(user.getId()).orElseThrow(() -> new AppException(USERNAME_NOT_FOUND, "해당 user 를 찾을 수 없습니다."));


        // 지정가 기준
        if(orderResponse.getOrd_type().equals("limit")) {

            // 매수 - 지정한 수량, 가격으로 매수
            if (orderResponse.getSide().equals("bid")) {

                TradingDiary tradingDiary = TradingDiary.builder()
                        .ord_type(orderResponse.getOrd_type())
                        .side(orderResponse.getSide())
                        .created_at(LocalDateTime.parse(orderResponse.getCreated_at().split("\\+")[0])) // 매수 주문시간
                        .market(orderResponse.getMarket())
                        .price(orderResponse.getPrice()) // 매수가격(수수료 포함)
                        .volume(Double.valueOf(orderResponse.getVolume())) // 내가 지정한 코인수량
                        .uuid(orderResponse.getUuid())
                        .user(user)
                        .build();

                tradingDiaryRepository.save(tradingDiary);
            }

            // 매도 - 지정한 수량, 가격으로 매도
            else if (orderResponse.getSide().equals("ask")) {

                TradingDiary tradingDiary = TradingDiary.builder()
                        .ord_type(orderResponse.getOrd_type())
                        .side(orderResponse.getSide())
                        .created_at(LocalDateTime.parse(orderResponse.getCreated_at().split("\\+")[0])) // 매수 주문시간
                        .market(orderResponse.getMarket())
                        .price(orderResponse.getPrice()) // 매수가격(수수료 포함)
                        .volume(Double.valueOf(orderResponse.getVolume())) // 내가 지정한 코인수량
                        .uuid(orderResponse.getUuid())
                        .user(user)
                        .build();

                tradingDiaryRepository.save(tradingDiary);
            }

        }


           // 매수 - 시장가 기준 (금액 만큼 팔았을 경우)
            if (orderResponse.getSide().equals("bid")) {

                TradingDiary tradingDiary = TradingDiary.builder()
                        .ord_type(orderResponse.getOrd_type())
                        .side(orderResponse.getSide())
                        .created_at(LocalDateTime.parse(orderResponse.getCreated_at().split("\\+")[0])) // 매수 주문시간
                        .market(orderResponse.getMarket())
                        .price(orderResponse.getPrice()) // 매수가격
                        .volume(Double.valueOf(response.getExecuted_volume())) // 거래 된 수량 ( 금액에 맞춰서 구매 된 코인 수량)
                        .uuid(orderResponse.getUuid())
                        .user(user)
                        .build();

                tradingDiaryRepository.save(tradingDiary);
            }

            // 매도 - 가진 수량을 다 팔았을 경우 (시장가 기준)
            else if (orderResponse.getSide().equals("ask")) {

                TradingDiary tradingDiary = TradingDiary.builder()
                        .ord_type(orderResponse.getOrd_type())
                        .side(orderResponse.getSide())
                        .created_at(LocalDateTime.parse(orderResponse.getCreated_at().split("\\+")[0])) // 매수 주문시간
                        .market(orderResponse.getMarket())
                        .price(Integer.valueOf(orderResponse.getPrice())) // 매도가격
                        .volume(Double.valueOf(orderResponse.getVolume()))
                        .uuid(orderResponse.getUuid())// 거래 된 수량 ( 금액에 맞춰서 구매 된 코인 수량
                        .user(user)
                        .build();

                tradingDiaryRepository.save(tradingDiary);
            }



    }

    public TradingDiaryDto edit(Long id, String comment, String email) {

        User user = userRepository.findByEmail(email).orElseThrow(()-> new AppException(EMAIL_NOT_FOUND,"해당 user의 일지가 없습니다."));


        TradingDiary tradingDiary = tradingDiaryRepository.findByUserIdAndId(user.getId(),id)
                .orElseThrow(()-> new AppException(DIARY_NOT_FOUND,"해당 일지가 없습니다."));

        tradingDiary.setComment(comment);

        tradingDiaryRepository.save(tradingDiary);

        return TradingDiaryDto.builder()
                .message("메모가 작성되었습니다.")
                .build();
    }


    public TradingDiaryDto delete(String email, Long id) {

        User user = userRepository.findByEmail(email).orElseThrow(()-> new AppException(EMAIL_NOT_FOUND,"해당 user의 일지가 없습니다."));

        Optional<TradingDiary> tradingDiary = tradingDiaryRepository.findByUserIdAndId(user.getId(),id);
        tradingDiary.orElseThrow(() -> new AppException(ErrorCode.DIARY_NOT_FOUND, "해당 일지가 없습니다."));

        tradingDiaryRepository.deleteById(id);

        return TradingDiaryDto.builder()
                .message("일지가 삭제되었습니다.")
                .build();
    }

    //주문취소
    public TradingDiaryDto orderDelete(String email, OrderDeleteResponse orderDeleteResponse){
        User user = userRepository.findByEmail(email).orElseThrow(()-> new AppException(EMAIL_NOT_FOUND,"해당 user의 일지가 없습니다."));

        tradingDiaryRepository.deleteByUuidAndUserId(orderDeleteResponse.getUuid(),user.getId());

        return  TradingDiaryDto.builder()
            .message("주문이 취소되어 일지가 삭제되었습니다.")
            .build();
    }

}






