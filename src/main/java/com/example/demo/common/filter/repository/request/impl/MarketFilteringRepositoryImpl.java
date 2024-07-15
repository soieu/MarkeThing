package com.example.demo.common.filter.repository.request.impl;

import static com.example.demo.marketpurchaserequest.entity.QMarket.market;

import com.example.demo.common.filter.dto.marketpurchaserequest.MarketFilterDto;
import com.example.demo.common.filter.repository.BaseQuerydslRepository;
import com.example.demo.common.filter.repository.request.MarketFilteringRepository;
import com.example.demo.marketpurchaserequest.entity.Market;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class MarketFilteringRepositoryImpl extends BaseQuerydslRepository
        implements MarketFilteringRepository {

    public MarketFilteringRepositoryImpl(JPAQueryFactory queryFactory,
            EntityManager entityManager) {
        super(queryFactory, entityManager);
    }

    @Override
    public Page<Market> findAllByFilter(MarketFilterDto marketFilterDto,
            Pageable pageable) {
        JPQLQuery<Market> marketJPQLQuery
                = queryFactory.selectFrom(market)
                .where(region(marketFilterDto));

        return getPageImpl(pageable, marketJPQLQuery, Market.class);
    }

    private BooleanExpression region(MarketFilterDto marketFilterDto) {
        if (marketFilterDto.isAllSidoIncluded()) {
            return Expressions.asBoolean(true).isTrue();
        }
        if (!marketFilterDto.isAllSigunguIncluded()) {
            StringExpression idNumExpression = Expressions.asString(market.idNum);

            return marketFilterDto.getSigunguIds().stream()
                    .map(sigungu -> idNumExpression.eq(marketFilterDto.getSidoId() + sigungu))
                    .reduce(BooleanExpression::or) // 생성된 표현식들 or 연산자로 결합
                    .orElse(null); // 시군구 목록 비어있으면, null 반환 => 필터링 조건 없음 의미.
        }
        return market.idNum.like(marketFilterDto.getSidoId() + "%"); // 모든 시군구 포함일 경우
    }
}
