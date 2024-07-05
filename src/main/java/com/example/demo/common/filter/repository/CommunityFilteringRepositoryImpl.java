package com.example.demo.common.filter.repository;

import com.example.demo.common.filter.dto.CommunityFilterDto;
import com.example.demo.community.entity.Community;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CommunityFilteringRepositoryImpl extends BaseQuerydslRepository implements FilteringRepository<Community, CommunityFilterDto> {

    public CommunityFilteringRepositoryImpl(JPAQueryFactory queryFactory,
            EntityManager entityManager) {
        super(queryFactory, entityManager);
    }

    @Override
    public PageImpl<Community> findAllByFilter(CommunityFilterDto filterDto, Pageable pageable) {
//        JPQLQuery<Community> communityJPQLQuery
//                = queryFactory.selectFrom()

        return null;
    }
}
