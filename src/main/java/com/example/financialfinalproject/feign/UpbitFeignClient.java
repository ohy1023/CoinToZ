package com.example.financialfinalproject.feign;

import com.example.financialfinalproject.domain.upbit.dto.MarketDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name="upbit", url = "https://api.upbit.com/v1")
public interface UpbitFeignClient {

    @GetMapping("/market/all")
    List<MarketDto> getMarKet(@RequestParam("isDetails") Boolean isDetails);

}
