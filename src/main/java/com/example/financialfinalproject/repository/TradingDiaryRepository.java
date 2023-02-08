package com.example.financialfinalproject.repository;

import com.example.financialfinalproject.domain.entity.TradingDiary;
import com.example.financialfinalproject.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradingDiaryRepository extends JpaRepository<TradingDiary, Long>, TradingDiaryCustomRepository {
    TradingDiary findByMarket(String market);

    List<TradingDiary> findAllByUser(User user);
}
