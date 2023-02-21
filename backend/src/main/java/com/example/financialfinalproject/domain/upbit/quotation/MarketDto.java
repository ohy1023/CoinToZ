package com.example.financialfinalproject.domain.upbit.quotation;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
public class MarketDto {
    private String market;
    private String korean_name;
    private String english_name;
}
