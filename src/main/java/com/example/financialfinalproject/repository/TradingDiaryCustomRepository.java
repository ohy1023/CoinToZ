package com.example.financialfinalproject.repository;


import com.example.financialfinalproject.domain.entity.TradingDiary;
import com.example.financialfinalproject.domain.entity.User;
import org.springframework.stereotype.Repository;


@Repository
public interface TradingDiaryCustomRepository {

    Double findSumVolumeByUser(User user);
    Double findSumVolumeByMarketContainingAndUser(String market, User user);
}
