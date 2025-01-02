package com.friendzoo.api.domain.content.repository.querydsl;

import com.friendzoo.api.domain.content.entity.Content;
import com.friendzoo.api.dto.PageRequestDTO;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.friendzoo.api.domain.content.entity.QContent.*;
import static com.friendzoo.api.domain.content.entity.QContentImage.contentImage;
import static com.friendzoo.api.domain.product.entity.QProduct.product;

@Slf4j
@Repository
@RequiredArgsConstructor
public class ContentRepositoryImpl implements ContentRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<Content> findListBy(PageRequestDTO requestDTO) {

        Pageable pageable = PageRequest.of(
                requestDTO.getPage() - 1,  //페이지 시작 번호가 0부터 시작하므로
                requestDTO.getSize(),
                "asc".equals(requestDTO.getSort()) ?  // 정렬 조건
                        Sort.by("id").ascending() : Sort.by("id").descending()
        );

        // pageable의 sort 정보를 적용
        OrderSpecifier[] orderSpecifiers = createOrderSpecifier(pageable.getSort());
        
        List<Content> list = queryFactory
                .select(content)
                .from(content)
                .leftJoin(content.imageList, contentImage).on(contentImage.ord.eq(0))
                .where(
                        content.delFlag.eq(false),
                        containsKeyword(requestDTO.getSearchTerm()),
                        eqDivisionId(requestDTO.getDivisionId())
                )
                .orderBy(orderSpecifiers)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // pageable의 sort 정보를 querydsl에 적용

        JPAQuery<Content> countQuery = queryFactory
                .select(content)
                .from(content)
                .leftJoin(content.imageList, contentImage).on(contentImage.ord.eq(0))
                .where(
                        content.delFlag.eq(false),
                        containsKeyword(requestDTO.getSearchTerm()),
                        eqDivisionId(requestDTO.getDivisionId())
                );

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchCount);
    }

    @Override
    public List<Content> findByIdList(List<Long> idList) {
        return queryFactory
                .selectFrom(content)
                .leftJoin(content.imageList).fetchJoin()
                .where(content.id.in(idList))
                .fetch();
    }


    /**
     * Sort 정보를 OrderSpecifier 배열로 변환
     * @param sort Sort 정보
     * @return OrderSpecifier 배열
     */
    private OrderSpecifier [] createOrderSpecifier(Sort sort) {
        return sort.stream()
                .map(order -> new OrderSpecifier(
                        order.isAscending() ? Order.ASC : Order.DESC,
                        new PathBuilder<>(Content.class, "content").get(order.getProperty())
                ))
                .toArray(OrderSpecifier[]::new);
    }


    private BooleanExpression eqDivisionId(Long categoryId) {
        if(categoryId == null) {
            return null;
        }
        return product.category.id.eq(categoryId);
    }

    private BooleanExpression containsKeyword(String keyword) {
        if(keyword == null || keyword.isBlank()) {
            return null;
        }
        return content.title.like("%" + keyword + "%");
    }
}
