package com.friendzoo.api.domain.order.repository.querydsl;

import com.friendzoo.api.domain.order.entity.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.List;

import static com.friendzoo.api.domain.order.entity.QOrder.order;


@Slf4j
@Repository
@RequiredArgsConstructor
public class OrderQuerydslRepository {

    private final JPAQueryFactory queryFactory;


    public Page<Order> findBySearch(String email, Pageable pageable, String searchTerm, Integer year) {

        // pageable의 sort 정보를 적용
        Sort sort = pageable.getSort();
        log.info("sort: {}", sort);

        List<Order> list = queryFactory
                .select(order)
                .from(order)
                .where(order.member.email.eq(email),
                        getSearchTermContains(searchTerm),
                        getEqOrderDateOfYear(year))
                .orderBy(getOrderSpecifier(sort))
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Order> countQuery = queryFactory
                .select(order)
                .from(order)
                .where(order.member.email.eq(email),
                        getSearchTermContains(searchTerm),
                        getEqOrderDateOfYear(year));

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchCount);

    }


    /**
     * 검색 조건을 생성
     *
     * @param searchTerm 검색어
     * @return 검색 조건
     */
    private BooleanExpression getSearchTermContains(String searchTerm) {
        if (StringUtils.hasText(searchTerm)) {
            return order.orderItems.any().product.name.contains(searchTerm.trim());
        }
        return null;
    }

    /**
     * 연도에 해당하는 주문일자와 일치하는지 검사
     *
     * @param year 연도
     * @return 연도에 해당하는 주문일자와 일치하는지 검사하는 조건
     */
    private BooleanExpression getEqOrderDateOfYear(Integer year) {
        if (year == null) {
            // 현재 시점에서 6개월 전을 검색
            return null;

        }
        return order.orderDate.year().eq(year);
    }

    /**
     * Sort 정보를 OrderSpecifier 배열로 변환
     *
     * @param sort Sort 정보
     * @return OrderSpecifier 배열
     */

    private OrderSpecifier<Long> getOrderSpecifier(Sort sort) {
        return sort.equals(Sort.by(Sort.Order.asc("id"))) ? order.id.asc() : order.id.desc();
    }


}
