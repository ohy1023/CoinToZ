package com.example.financialfinalproject.controller.restController;
import com.example.financialfinalproject.domain.upbit.dto.MarketDto;
import com.example.financialfinalproject.service.UpbitService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
public class UpbitRestController {

    private final UpbitService upbitService;

    @GetMapping("/getMarket")
    @ApiOperation(value = "마켓 코드 조회", notes = "업비트에서 거래 가능한 마켓 목록")
    public List<MarketDto> getMarket(@RequestParam(defaultValue = "false") Boolean isDetails){
        List<MarketDto> markets = upbitService.getMarket(isDetails);
        return markets;
    }

}
