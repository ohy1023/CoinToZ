package com.example.financialfinalproject.repository;

import com.example.financialfinalproject.domain.entity.TradingDiary;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TradingDiaryRepository extends JpaRepository <TradingDiary, Long> {
    TradingDiary findByMarket(String market);
}
