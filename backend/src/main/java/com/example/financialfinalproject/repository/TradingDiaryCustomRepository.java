package com.example.financialfinalproject.repository;


import com.example.financialfinalproject.domain.dto.TradingDiaryListDto;
import com.example.financialfinalproject.domain.entity.TradingDiary;
import com.example.financialfinalproject.domain.entity.User;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface TradingDiaryCustomRepository {

    List<TradingDiary> searchDateRange(User user, LocalDateTime startDate, LocalDateTime endDate);

    List<TradingDiary> findAllByUserOrderByDate(User user);


}
