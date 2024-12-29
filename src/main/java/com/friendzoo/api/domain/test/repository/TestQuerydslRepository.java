package com.friendzoo.api.domain.test.repository;


import com.friendzoo.api.domain.test.entity.Test;
import com.friendzoo.api.dto.PageRequestDTO;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.friendzoo.api.domain.test.entity.QTest.test;


@Repository
@RequiredArgsConstructor
public class TestQuerydslRepository {

    private final JPAQueryFactory queryFactory;


    public Page<Test> findAll(Pageable pageable, String keyword) {

        // List
        List<Test> list = queryFactory
                .select(test)
                .from(test)
                .where(containsTitle(keyword))
//                .orderBy(test.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();


        // total count
        JPAQuery<Test> countQuery = queryFactory
                .selectFrom(test)
                .where(containsTitle(keyword));//                .where()

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchCount); // list, total count
    }

    public Page<Test> findListBy(PageRequestDTO requestDTO) {

        Pageable pageable = PageRequest.of(
                requestDTO.getPage() - 1,  //페이지 시작 번호가 0부터 시작하므로
                requestDTO.getSize(),
                "asc".equals(requestDTO.getSort()) ?  // 정렬 조건
                        Sort.by("pno").ascending() : Sort.by("pno").descending()
        );

        List<Test> list = queryFactory
                .select(test)
                .from(test)
                .where(
                        containsTitle(requestDTO.getSearchTerm())
                )
//                .orderBy(requestDTO.getSort().getOrderSpecifier())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        JPAQuery<Test> countQuery = queryFactory
                .selectFrom(test)
                .where(
                        containsTitle(requestDTO.getSearchTerm())
                );

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchCount);
    }

    private BooleanExpression containsTitle(String title) {

        if(title == null || title.isBlank()) {
            return null;
        }
        return test.title.contains(title);
    }
}

