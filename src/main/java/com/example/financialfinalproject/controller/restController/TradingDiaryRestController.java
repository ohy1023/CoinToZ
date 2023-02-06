package com.example.financialfinalproject.controller.restController;

import com.example.financialfinalproject.domain.dto.TradingDiaryDto;
import com.example.financialfinalproject.domain.entity.TradingDiary;
import com.example.financialfinalproject.domain.request.DiaryPutRequest;
import com.example.financialfinalproject.service.TradingDiaryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/diary")
public class TradingDiaryRestController {

    private final TradingDiaryService tradingDiaryService;

    // 매매일지 메모 수정
    @PutMapping("/edit/{id}")
    public TradingDiaryDto edit(@PathVariable Long id, @RequestBody DiaryPutRequest request) {
        log.info("comment:{}",request.getComment());
        TradingDiaryDto tradingDiaryDto = tradingDiaryService.edit(id, request.getComment());
        return tradingDiaryDto;
    }

    // 매매일지 리스트
    @GetMapping("/list")

    public Page<TradingDiary> list() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<TradingDiary> tradingDiaryList = tradingDiaryService.list(pageRequest);
        return tradingDiaryList;
    }

    @GetMapping("/list2")
    public List<TradingDiary> list2() {
        List<TradingDiary> tradingDiaryList = tradingDiaryService.listOf();
        return tradingDiaryList;
    }


    // 매매일지 삭제
    @DeleteMapping("/delete")
    public TradingDiaryDto delete(Long id) {
        TradingDiaryDto tradingDiaryDto = tradingDiaryService.delete(id);
        return tradingDiaryDto;
    }

}
