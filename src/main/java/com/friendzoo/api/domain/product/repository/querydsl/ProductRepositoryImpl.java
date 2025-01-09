package com.friendzoo.api.domain.product.repository.querydsl;

import com.friendzoo.api.domain.product.entity.Product;
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

import java.util.List;

import static com.friendzoo.api.domain.content.entity.QContent.content;
import static com.friendzoo.api.domain.heart.entity.QHeart.heart;
import static com.friendzoo.api.domain.product.entity.QProduct.product;
import static com.friendzoo.api.domain.product.entity.QProductImage.productImage;


@Slf4j
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProductRepositoryCustom {


    private final JPAQueryFactory queryFactory;


    @Override
    public Page<Product> findListBy(PageRequestDTO requestDTO) {

        Pageable pageable = PageRequest.of(
                requestDTO.getPage() - 1,  //페이지 시작 번호가 0부터 시작하므로
                requestDTO.getSize(),
                "asc".equals(requestDTO.getSort()) ?  // 정렬 조건
                        Sort.by("id").ascending() : Sort.by("id").descending()
        );

        // pageable의 sort 정보를 적용
        OrderSpecifier[] orderSpecifiers = createOrderSpecifier(pageable.getSort());


        List<Product> list = queryFactory
                .select(product)
                .from(product)
                .leftJoin(product.imageList, productImage).on(productImage.ord.eq(0))
                .where(
                        product.delFlag.eq(false),
                        containsKeyword(requestDTO.getSearchTerm()),
                        eqCategoryId(requestDTO.getCategoryId())
                )
                .orderBy(orderSpecifiers)
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        // pageable의 sort 정보를 querydsl에 적용

        JPAQuery<Product> countQuery = queryFactory
                .select(product)
                .from(product)
                .leftJoin(product.imageList, productImage).on(productImage.ord.eq(0))
                .where(
                        product.delFlag.eq(false),
                        containsKeyword(requestDTO.getSearchTerm()),
                        eqCategoryId(requestDTO.getCategoryId())
                );

        return PageableExecutionUtils.getPage(list, pageable, countQuery::fetchCount);
    }




    @Override
    public List<Product> findByIdList(List<Long> idList) {
        return queryFactory.selectFrom(product)
                .where(product.id.in(idList))
                .fetch();
    }

    @Override
    public Product findDetailProduct(String email,Long productId){
        return queryFactory
                .select(product)
                .from(product)
                .leftJoin(product.imageList, productImage).on(productImage.ord.eq(0))
                .leftJoin(product.heartList, heart).fetchJoin()
                .where(
                        product.delFlag.eq(false),
                        product.id.eq(productId),
                        product.heartList.any().member.email.eq(email)
                )
                .fetchOne();
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
                        new PathBuilder<>(Product.class, "product").get(order.getProperty())
                ))
                .toArray(OrderSpecifier[]::new);
    }

    private BooleanExpression eqCategoryId(Long categoryId) {
        if(categoryId == null) {
            return null;
        }
        return product.category.id.eq(categoryId);
    }

    private BooleanExpression containsKeyword(String keyword) {
        if(keyword == null || keyword.isBlank()) {
            return null;
        }
        return product.name.like("%" + keyword + "%");
    }


}
