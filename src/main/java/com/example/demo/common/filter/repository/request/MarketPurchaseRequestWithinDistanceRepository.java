package com.example.demo.common.filter.repository.request;

import com.example.demo.marketpurchaserequest.entity.MarketPurchaseRequest;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface MarketPurchaseRequestWithinDistanceRepository {
    Page<MarketPurchaseRequest> findAllWithinBoundary(Point myLocation,
            Point northEastBound, Point southWestBound, Pageable pageable);
}
