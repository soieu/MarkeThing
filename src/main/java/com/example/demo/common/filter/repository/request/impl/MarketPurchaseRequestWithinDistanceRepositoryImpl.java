package com.example.demo.common.filter.repository.request.impl;

import static com.example.demo.marketpurchaserequest.entity.QMarketPurchaseRequest.marketPurchaseRequest;

import com.example.demo.common.filter.repository.BaseQuerydslRepository;
import com.example.demo.common.filter.repository.request.MarketPurchaseRequestWithinDistanceRepository;
import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import com.example.demo.type.PurchaseRequestStatus;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.StringTemplate;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class MarketPurchaseRequestWithinDistanceRepositoryImpl
        extends BaseQuerydslRepository implements MarketPurchaseRequestWithinDistanceRepository {

    public MarketPurchaseRequestWithinDistanceRepositoryImpl(
            JPAQueryFactory queryFactory, EntityManager entityManager) {
        super(queryFactory, entityManager);
    }

    @Override
    public PageImpl<MarketPurchaseRequest> findAllWithinBoundary(Point myLocation,
            Point northEastBound,
            Point southWestBound, Pageable pageable) {


        BooleanExpression withinBoundary =
                within(southWestBound, northEastBound);

        StringTemplate distanceTemplate = Expressions.stringTemplate(HAVERSINE_FORMULA,
                marketPurchaseRequest.meetupLon, marketPurchaseRequest.meetupLat,
                myLocation.getX(), myLocation.getY());

        JPQLQuery<MarketPurchaseRequest> marketPurchaseRequestJPQLQuery = queryFactory
                .selectFrom(marketPurchaseRequest)
                .where(withinBoundary)
                .orderBy(distanceTemplate.asc());

        return getPageImpl(pageable, marketPurchaseRequestJPQLQuery, MarketPurchaseRequest.class);
    }

    private BooleanExpression within(Point southWestBound, Point northEastBound) {
        return marketPurchaseRequest.meetupLat.between(southWestBound.getY(),
                        northEastBound.getY())
                .and(marketPurchaseRequest.meetupLon.between(southWestBound.getX(),
                        northEastBound.getX()))
                .and(marketPurchaseRequest.purchaseRequestStatus.ne(
                        PurchaseRequestStatus.COMPLETED));

    }
}
