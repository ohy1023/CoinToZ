package com.example.financialfinalproject.controller;

import com.example.financialfinalproject.domain.upbit.dto.MarketDto;
import com.example.financialfinalproject.service.UpbitService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class MarketController {

    private final UpbitService upbitService;

    @GetMapping("/market")
    // @ApiOperation(value = "마켓 코드 조회", notes = "업비트에서 거래 가능한 마켓 목록")
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

}
