package com.example.financialfinalproject.repository;

import com.example.financialfinalproject.domain.entity.TradingDiary;
import com.example.financialfinalproject.domain.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.financialfinalproject.domain.entity.QTradingDiary.*;


@Repository
@RequiredArgsConstructor
public class TradingDiaryRepositoryImpl implements TradingDiaryCustomRepository {

    private final JPAQueryFactory query;

    @Override
    public List<TradingDiary> searchDateRange(User user, LocalDateTime startDate, LocalDateTime endDate) {
        return query.select(tradingDiary)
                .from(tradingDiary)
                .where(eqUser(user), betweenDate(startDate, endDate))
                .orderBy(tradingDiary.created_at.desc())
                .fetch();
    }

    @Override
    public List<TradingDiary> findAllByUserOrderByDate(User user) {
        return query.selectFrom(tradingDiary)
                .where(eqUser(user))
                .orderBy(tradingDiary.created_at.desc())
                .fetch();
    }

    private BooleanExpression betweenDate(LocalDateTime start, LocalDateTime end) {

        if (start == null || end == null) {
            return null;
        }
        return tradingDiary.created_at.between(start, end);
    }

    private BooleanExpression eqUser(User user) {
        if (user == null) {
            return null;
        }
        return tradingDiary.user.eq(user);
    }

    private BooleanExpression eqMarket(String market) {
        if (market == null || market.isEmpty()) {
            return null;
        }
        return tradingDiary.market.eq(market);
    }
}
