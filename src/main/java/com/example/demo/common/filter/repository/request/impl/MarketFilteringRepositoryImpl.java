package com.example.demo.common.filter.repository.request.impl;

import static com.example.demo.marketpurchaserequest.entity.QMarket.market;
import static com.example.demo.marketpurchaserequest.entity.QMarketPurchaseRequest.marketPurchaseRequest;

import com.example.demo.common.filter.dto.marketpurchaserequest.MarketFilterDto;
import com.example.demo.common.filter.dto.marketpurchaserequest.MarketPurchaseRequestFilterDto;
import com.example.demo.common.filter.repository.BaseQuerydslRepository;
import com.example.demo.common.filter.repository.request.MarketFilteringRepository;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
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
    public Page<MarketPurchaseRequest> findAllByFilter(MarketFilterDto marketFilterDto,
            Pageable pageable) {
//        JPQLQuery<MarketPurchaseRequest> marketPurchaseRequestJPQLQuery
//                = queryFactory.selectFrom(market)
//                .where(region(marketFilterDto));
//
//        return getPageImpl(pageable, marketPurchaseRequestJPQLQuery, MarketPurchaseRequest.class);
        return null;
    }

    private BooleanExpression region(MarketFilterDto marketFilterDto) {
        if (marketFilterDto.isEmpty()) {
            return Expressions.asBoolean(true).isTrue();
        }
        return null;
    }
}
