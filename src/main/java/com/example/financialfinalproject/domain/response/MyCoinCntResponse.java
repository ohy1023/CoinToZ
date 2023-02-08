package com.example.financialfinalproject.domain.response;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MyCoinCntResponse {
    private double BitCoin;
    private double Ethereum;
    private double Ripple;
    private double ADA;
    private double DogeCoin;
    private double Etc;

    @Builder
    public MyCoinCntResponse(double bitCoin, double ethereum, double ripple, double ADA, double dogeCoin, double etc) {
        BitCoin = bitCoin;
        Ethereum = ethereum;
        Ripple = ripple;
        this.ADA = ADA;
        DogeCoin = dogeCoin;
        Etc = etc;
    }
}
