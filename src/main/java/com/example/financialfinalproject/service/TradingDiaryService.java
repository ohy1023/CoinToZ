package com.example.financialfinalproject.service;

import com.example.financialfinalproject.domain.dto.TradingDiaryDto;
import com.example.financialfinalproject.domain.entity.TradingDiary;
import com.example.financialfinalproject.domain.upbit.exchange.OrderResponse;
import com.example.financialfinalproject.exception.AppException;
import com.example.financialfinalproject.exception.ErrorCode;
import com.example.financialfinalproject.repository.TradingDiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class TradingDiaryService {

    private final TradingDiaryRepository tradingDiaryRepository;

    public void write(OrderResponse orderResponse) {


        // 수수료 반올림
        int fee = (int) Math.round(orderResponse.getPaid_fee());


        // 매수
        if (orderResponse.getSide().equals("bid")) {

            TradingDiary tradingDiary = TradingDiary.builder()
                    .bid_created_at(orderResponse.getCreated_at()) // 매수 주문시간
                    .market(orderResponse.getMarket())
                    .bid_price(orderResponse.getPrice() + fee) // 매수가격(수수료 포함)
                    .volume(orderResponse.getExecuted_volume()) // 수량
                    .build();

            tradingDiaryRepository.save(tradingDiary);
        }

        // 매도
        else if (orderResponse.getSide().equals("ask")) {

            TradingDiary tradingDiarys = tradingDiaryRepository.findByMarket(orderResponse.getMarket());
            tradingDiarys.setAsk_created_at(orderResponse.getCreated_at()); // 매도시간
            tradingDiarys.setAsk_price(orderResponse.getPrice() + fee); // 매도가격(수수료 포함)


            tradingDiarys.setArbitrage(tradingDiarys.getAsk_price()- tradingDiarys.getBid_price()); // 차익 (매수가격 - 매도가격)

            double revenue = ((double)tradingDiarys.getBid_price() / (double)tradingDiarys.getArbitrage()); // 수익률 계산
            tradingDiarys.setRevenue(Math.round(revenue*100)/100.0); // 수익률 (소숫점 2자리 까지 표현)

            tradingDiaryRepository.save(tradingDiarys);
        }
    }

    public TradingDiaryDto edit(Long id, String comment) {

        Optional<TradingDiary> tradingDiary = tradingDiaryRepository.findById(id);
        tradingDiary.orElseThrow(()-> new AppException(ErrorCode.DIARY_NOT_FOUND,"해당 일지가 없습니다."));

        TradingDiary tradingDiaryEdit = tradingDiary.get();

        tradingDiaryEdit.setComment(comment);

        tradingDiaryRepository.save(tradingDiaryEdit);


        return TradingDiaryDto.builder()
                .message("메모가 작성되었습니다.")
                .build();
    }


    public Page<TradingDiary> list(PageRequest pageRequest){
    Page<TradingDiary> tradingDiaryList = tradingDiaryRepository.findAll(pageRequest);
    return tradingDiaryList;
    }


    public TradingDiaryDto delete(Long id){

       Optional<TradingDiary> tradingDiary = tradingDiaryRepository.findById(id);
       tradingDiary.orElseThrow(()-> new AppException(ErrorCode.DIARY_NOT_FOUND,"해당 일지가 없습니다."));

        tradingDiaryRepository.deleteById(id);

        return TradingDiaryDto.builder()
                .message("일지가 삭제되었습니다.")
                .build();
    }


}




