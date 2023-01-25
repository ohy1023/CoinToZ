package com.example.financialfinalproject.global.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.financialfinalproject.domain.upbit.UpbitToken;
import org.springframework.stereotype.Service;

import java.util.UUID;


@Service
public class UpbitJwtService {

    public UpbitToken getToken(String accessKey, String secretKey){
        Algorithm algorithm = Algorithm.HMAC256(secretKey);

        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        return UpbitToken.builder()
                .upbitToken(authenticationToken)
                .build();
    }

}
