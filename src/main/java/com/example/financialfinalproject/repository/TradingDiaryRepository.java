package com.example.financialfinalproject.repository;

import com.example.financialfinalproject.domain.entity.TradingDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradingDiaryRepository extends JpaRepository<TradingDiary, Long>, TradingDiaryCustomRepository {
    TradingDiary findByMarket(String market);
    TradingDiary deleteByBidUuid(String uuid);
    TradingDiary deleteByAskUuid(String uuid);

}
