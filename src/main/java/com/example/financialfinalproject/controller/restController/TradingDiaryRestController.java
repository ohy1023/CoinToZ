package com.example.financialfinalproject.controller.restController;

import com.example.financialfinalproject.domain.dto.TradingDiaryDto;
import com.example.financialfinalproject.domain.entity.TradingDiary;
import com.example.financialfinalproject.service.TradingDiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class TradingDiaryRestController {

    private final TradingDiaryService tradingDiaryService;

    // 매매일지 메모 수정
    @PutMapping("/diary/edit")
    public TradingDiaryDto edit(Long id, String comment){

        TradingDiaryDto tradingDiaryDto = tradingDiaryService.edit(id,comment);
        return tradingDiaryDto;
    }

    // 매매일지 리스트
    @GetMapping("/diary/list")

    public Page<TradingDiary> list(){
        PageRequest pageRequest = PageRequest.of(0,10);
        Page<TradingDiary> tradingDiaryList = tradingDiaryService.list(pageRequest);
        return tradingDiaryList;
    }


    // 매매일지 삭제
    @DeleteMapping("/diary/delete")
    public TradingDiaryDto delete(Long id){
        TradingDiaryDto tradingDiaryDto = tradingDiaryService.delete(id);
        return tradingDiaryDto;
    }

}
