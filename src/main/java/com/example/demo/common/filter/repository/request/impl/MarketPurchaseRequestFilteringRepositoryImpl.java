package com.example.demo.common.filter.repository.request.impl;

import static com.example.demo.marketpurchaserequest.entity.QMarketPurchaseRequest.marketPurchaseRequest;

import com.example.demo.common.filter.dto.KeywordDto;
import com.example.demo.common.filter.repository.BaseQuerydslRepository;
import com.example.demo.common.filter.repository.request.MarketPurchaseRequestFilteringRepository;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class MarketPurchaseRequestFilteringRepositoryImpl extends BaseQuerydslRepository
        implements MarketPurchaseRequestFilteringRepository {

    public MarketPurchaseRequestFilteringRepositoryImpl(JPAQueryFactory queryFactory,
            EntityManager entityManager) {
        super(queryFactory, entityManager);
    }

    @Override
    public PageImpl<MarketPurchaseRequest> findAllByFilter(KeywordDto keywordDto,
            Pageable pageable) {
        JPQLQuery<MarketPurchaseRequest> marketPurchaseRequestJPQLQuery
                = queryFactory.selectFrom(marketPurchaseRequest)
                .where(keyword(keywordDto));

        return getPageImpl(pageable, marketPurchaseRequestJPQLQuery, MarketPurchaseRequest.class);
    }

    private BooleanExpression keyword(KeywordDto keywordDto) {
        return
                marketPurchaseRequest.title.containsIgnoreCase(keywordDto.getKeyword())
                        .or(marketPurchaseRequest.content.containsIgnoreCase(
                                keywordDto.getKeyword()))
                        .or(marketPurchaseRequest.market.marketName.containsIgnoreCase(
                                keywordDto.getKeyword()))
                        .or(marketPurchaseRequest.siteUser.nickname.containsIgnoreCase(
                                keywordDto.getKeyword()));
    }
}
