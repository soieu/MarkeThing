package com.example.demo.common.filter.repository.community;

import static com.example.demo.community.entity.QCommunity.community;

import com.example.demo.common.filter.dto.community.CommunityFilterDto;
import com.example.demo.common.filter.repository.BaseQuerydslRepository;
import com.example.demo.community.entity.Community;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import javax.persistence.EntityManager;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public class CommunityFilteringRepositoryImpl extends BaseQuerydslRepository
        implements CommunityFilteringRepository {

    public CommunityFilteringRepositoryImpl(JPAQueryFactory queryFactory,
            EntityManager entityManager) {
        super(queryFactory, entityManager);
    }

    @Override
    public PageImpl<Community> findAllByFilter(CommunityFilterDto communityFilterDto, Pageable pageable) {
        JPQLQuery<Community> communityJPQLQuery
                = queryFactory.selectFrom(community)
                .where(area(communityFilterDto));

        return getPageImpl(pageable, communityJPQLQuery, Community.class);
    }

    private BooleanExpression area(CommunityFilterDto communityFilterDto) {
        if(communityFilterDto.getAreas().isEmpty()) { // 필터 없는 상태랑 같음.
            return Expressions.asBoolean(true).isTrue(); // 항상 참인 조건을 나타내는 표현식 반환
        }
        return community.area.in(communityFilterDto.getAreas());
    }
}
