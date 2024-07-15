package com.example.demo.common.filter.repository;

import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.core.types.dsl.PathBuilderFactory;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import javax.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.Querydsl;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public abstract class BaseQuerydslRepository {
    protected final JPAQueryFactory queryFactory; // Querydsl 쿼리 생성하는 데 사용
    protected final EntityManager entityManager; // 엔티티의 생명 주기 관리, DB 작업 수행
    protected static final String HAVERSINE_FORMULA = "ST_Distance_Sphere(point({0}, {1}), point({2}, {3}))"; // 두 좌표 사이의 거리 구하는 공식

    protected <T> Querydsl getQuerydsl(Class<T> clazz) { // T: 엔티티 타입, Class<T>: 쿼리 빌더 생성할 클래스 타입
        PathBuilder<T> builder = new PathBuilderFactory().create(clazz); // 주어진 클래스 타입에 대해 PathBuilder 생성,
        return new Querydsl(entityManager, builder); // 반환된 Querydsl 객체는 퀴리 및 페이징 처리에 사용
    }

    protected <T> PageImpl<T> getPageImpl(Pageable pageable, JPQLQuery<T> query, Class<T> clazz) {
        long totalCount = query.fetchCount(); // 전체 결과 수
        List<T> results = getQuerydsl(clazz) // 위에서 정의한 메서드로 Querydsl 객체 생성
                .applyPagination(pageable, query).fetch(); // 페이징 정보 적용한 결과 리스트
        return new PageImpl<>(results, pageable, totalCount); // 전체 결과 수와 페이징 정보 기반으로 PageImpl 객체 생성 후 반환
    }
}
