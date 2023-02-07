package com.example.financialfinalproject.repository;

import com.example.financialfinalproject.domain.entity.QTradingDiary;
import com.example.financialfinalproject.domain.entity.User;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import static com.example.financialfinalproject.domain.entity.QTradingDiary.*;


@Repository
@RequiredArgsConstructor
public class TradingDiaryRepositoryImpl implements TradingDiaryCustomRepository{

    private final JPAQueryFactory query;

    @Override
    public Double findSumVolumeByUser(User user) {
        return query.select(tradingDiary.volume.sum())
                .from(tradingDiary)
                .where(eqUser(user))
                .fetchOne();
    }

    @Override
    public Double findSumVolumeByMarketContainingAndUser(String market, User user) {
        return query.select(tradingDiary.volume.sum())
                .from(tradingDiary)
                .where(eqMarket(market),eqUser(user))
                .fetchOne();
    }
    private BooleanExpression eqUser(User user) {
        if(user == null) {
            return null;
        }
        return tradingDiary.user.eq(user);
    }

    private BooleanExpression eqMarket(String market) {
        if(market == null || market.isEmpty()) {
            return null;
        }
        return tradingDiary.market.eq(market);
    }
}
