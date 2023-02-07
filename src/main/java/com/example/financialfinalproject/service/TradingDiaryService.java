package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.dto.TradingDiaryDto;
import com.example.financialfinalproject.domain.entity.TradingDiary;
import com.example.financialfinalproject.domain.entity.User;
import com.example.financialfinalproject.domain.response.MyCoinCntResponse;
import com.example.financialfinalproject.domain.upbit.exchange.OrderResponse;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.exception.ErrorCode;
import com.example.financialfinalproject.repository.TradingDiaryRepository;
import com.example.financialfinalproject.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.example.financialfinalproject.exception.ErrorCode.EMAIL_NOT_FOUND;

@RequiredArgsConstructor
@Service
public class TradingDiaryService {

    private final TradingDiaryRepository tradingDiaryRepository;
    private final UserRepository userRepository;

    private String BitCoin = "KRW-BTC";
    private String Ethereum = "KRW-ETH";
    private String Ripple = "KRW-XRP";
    private String ADA = "KRW-ADA";
    private String DogeCoin = "KRW-DOGE";


    public List<TradingDiary> listOf(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
                });
        List<TradingDiary> diaryListOfUser = tradingDiaryRepository.findAllByUser(user);
        return diaryListOfUser;
    }

    public MyCoinCntResponse getCoins(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> {
                    throw new AppException(EMAIL_NOT_FOUND, EMAIL_NOT_FOUND.getMessage());
                });

        double bitCoinCnt = tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(BitCoin, user);
        double ethereumCnt = tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(Ethereum, user);
        double rippleCnt = tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(Ripple, user);
        double ADACnt = tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(ADA, user);
        double dogeCoinCnt = tradingDiaryRepository.findSumVolumeByMarketContainingAndUser(DogeCoin, user);
        double etcCnt = tradingDiaryRepository.findSumVolumeByUser(user) - (bitCoinCnt + ethereumCnt + rippleCnt + ADACnt + dogeCoinCnt);

        return MyCoinCntResponse.builder()
                .bitCoin(bitCoinCnt)
                .ethereum(ethereumCnt)
                .ADA(ADACnt)
                .dogeCoin(dogeCoinCnt)
                .ripple(rippleCnt)
                .etc(etcCnt)
                .build();

    }

    public void write(OrderResponse orderResponse) {


        // 수수료 반올림
        int fee = (int) Math.round(orderResponse.getPaid_fee());


        // 매수
        if (orderResponse.getSide().equals("bid")) {

            TradingDiary tradingDiary = TradingDiary.builder()
                    .bid_created_at(orderResponse.getCreated_at()) // 매수 주문시간
                    .market(orderResponse.getMarket())
                    .bid_price(orderResponse.getPrice() + fee) // 매수가격(수수료 포함)
                    .volume(Double.parseDouble(orderResponse.getExecuted_volume())) // 수량
                    .build();

            tradingDiaryRepository.save(tradingDiary);
        }

        // 매도
        else if (orderResponse.getSide().equals("ask")) {

            TradingDiary tradingDiarys = tradingDiaryRepository.findByMarket(orderResponse.getMarket());
            tradingDiarys.setAsk_created_at(orderResponse.getCreated_at()); // 매도시간
            tradingDiarys.setAsk_price(orderResponse.getPrice() + fee); // 매도가격(수수료 포함)


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


    public Page<TradingDiary> list(PageRequest pageRequest) {
        Page<TradingDiary> tradingDiaryList = tradingDiaryRepository.findAll(pageRequest);
        return tradingDiaryList;
    }


    public TradingDiaryDto delete(Long id) {

        Optional<TradingDiary> tradingDiary = tradingDiaryRepository.findById(id);
        tradingDiary.orElseThrow(() -> new AppException(ErrorCode.DIARY_NOT_FOUND, "해당 일지가 없습니다."));

        tradingDiaryRepository.deleteById(id);

        return TradingDiaryDto.builder()
                .message("일지가 삭제되었습니다.")
                .build();
    }


}




