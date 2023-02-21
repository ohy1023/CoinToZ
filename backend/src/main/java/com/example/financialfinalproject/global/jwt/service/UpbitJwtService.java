package com.example.financialfinalproject.global.jwt.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.financialfinalproject.domain.upbit.exchange.CoinWithDrawRequest;
import com.example.financialfinalproject.domain.upbit.exchange.KrwWithDrawRequest;
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

    //주문
    public UpbitToken getOrderToken(String accessKey, String secretKey, OrderRequest orderRequest) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        //주문값 형태 변경
        HashMap<String, String> params = new HashMap<>();
        params.put("market", orderRequest.getMarket());
        params.put("side", orderRequest.getSide());
        params.put("volume", orderRequest.getVolume());
        params.put("price", orderRequest.getPrice());
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

 // 코인출금
    public UpbitToken getWithDrawCoinToken(String accessKey, String secretKey, CoinWithDrawRequest coinWithDrawRequest) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //출금 요청 형태 변경
        HashMap<String, String> params = new HashMap<>();
        params.put("currency", coinWithDrawRequest.getCurrency());
        params.put("amount", coinWithDrawRequest.getAmount());
        params.put("address", String.valueOf(coinWithDrawRequest.getAddress()));
        params.put("secondary_address", String.valueOf(coinWithDrawRequest.getSecondary_address()));
        params.put("transaction_type", coinWithDrawRequest.getTransaction_type());

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

    // 주문취소
    public  UpbitToken getOrderDeleteToken(String accessKey, String secretKey, String uuid) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        HashMap<String, String> params = new HashMap<>();
        params.put("uuid", uuid);


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


// 주문리스트
    public UpbitToken getOrderListToken(String accessKey, String secretKey, String state) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        //주문값 형태 변경
        HashMap<String, String> params = new HashMap<>();
        params.put("state", state);


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


    // 원화 출금
    public UpbitToken getWithDrawKrwToken(String accessKey, String secretKey, KrwWithDrawRequest krwWithDrawRequest) throws NoSuchAlgorithmException, UnsupportedEncodingException {
        //출금 요청 형태 변경
        HashMap<String, String> params = new HashMap<>();
        params.put("amount", krwWithDrawRequest.getAmount());
        params.put("two_factor_type", krwWithDrawRequest.getTwo_factor_type());

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


    // 입금
    public UpbitToken getDepositToken(String accessKey, String secretKey, String amount, String two_factor_type) throws NoSuchAlgorithmException, UnsupportedEncodingException {

        HashMap<String, String> params = new HashMap<>();
        params.put("amount", amount);
        params.put("two_factor_type", two_factor_type);


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
