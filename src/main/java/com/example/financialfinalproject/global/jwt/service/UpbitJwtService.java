package com.example.financialfinalproject.global.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.financialfinalproject.domain.upbit.exchange.OrderRequest;
import com.example.financialfinalproject.domain.upbit.exchange.UpbitToken;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


@Service
public class UpbitJwtService {

    public UpbitToken getToken(String accessKey, String secretKey) {
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

    public UpbitToken getOrderToken(String accessKey, String secretKey, OrderRequest orderRequest) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        //주문값 형태 변경
        HashMap<String, String> params = new HashMap<>();
        params.put("market", orderRequest.getMarket());
        params.put("side", orderRequest.getSide());
        params.put("volume", String.valueOf(orderRequest.getVolume()));
        params.put("price", String.valueOf(orderRequest.getPrice()));
        params.put("ord_type", orderRequest.getOrd_type());

        ArrayList<String> queryElements = new ArrayList<>();
        for (Map.Entry<String, String> entity : params.entrySet()) {
            queryElements.add(entity.getKey() + "=" + entity.getValue());
        }

        String queryString = String.join("&", queryElements.toArray(new String[0]));
        System.out.println(queryString);


        // 암호화
        MessageDigest md = MessageDigest.getInstance("SHA-512");
        md.update(queryString.getBytes("UTF-8"));
        String queryHash = String.format("%0128x", new BigInteger(1, md.digest()));
        Algorithm algorithm = Algorithm.HMAC256(secretKey);


        // 토큰 생성
        String jwtToken = JWT.create()
                .withClaim("access_key", accessKey)
                .withClaim("nonce", UUID.randomUUID().toString())
                .withClaim("query_hash", queryHash)
                .withClaim("query_hash_alg", "SHA512")
                .sign(algorithm);

        String authenticationToken = "Bearer " + jwtToken;

        return UpbitToken.builder()
                .upbitToken(authenticationToken)
                .build();
    }
}
