package com.example.financialfinalproject.repository;

import com.example.financialfinalproject.domain.entity.TradingDiary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TradingDiaryRepository extends JpaRepository<TradingDiary, Long>, TradingDiaryCustomRepository {
    TradingDiary findByMarket(String market);
    TradingDiary deleteByUuidAndUserId(String uuid, Integer userId);

    Optional<TradingDiary> findByUserIdAndId(Integer userId, Long commentId);

}
