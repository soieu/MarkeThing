package com.example.demo.util;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class PointUtils {

    private static final double EARTH_RADIUS_KM = 6371.0; // 지구 반지름 (단위: km)

    public static Point getBoudaryPoint(Point startPoint, double distanceKm,
            double bearingDegrees) {
        double lat1 = Math.toRadians(startPoint.getY());
        double lon1 = Math.toRadians(startPoint.getX());
        double bearingRadians = Math.toRadians(bearingDegrees);
        double angularDistance = distanceKm / EARTH_RADIUS_KM;

        double lat2 = Math.asin(Math.sin(lat1) * Math.cos(angularDistance) +
                Math.cos(lat1) * Math.sin(angularDistance) * Math.cos(bearingRadians));
        double lon2 = lon1 + Math.atan2(
                Math.sin(bearingRadians) * Math.sin(angularDistance) * Math.cos(lat1),
                Math.cos(angularDistance) - Math.sin(lat1) * Math.sin(lat2));

        lon2 = (lon2 + 3 * Math.PI) % (2 * Math.PI) - Math.PI; // 경도값이 -180 ~ +180도 사이에 있도록 정규화

        GeometryFactory geometryFactory = new GeometryFactory();
        return geometryFactory.createPoint(
                new Coordinate(Math.toDegrees(lon2), Math.toDegrees(lat2)));
    }
}
