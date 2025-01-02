package com.friendzoo.api.domain.order.service;

import com.friendzoo.api.domain.cart.dto.CartItemDTO;
import com.friendzoo.api.domain.member.entity.Member;
import com.friendzoo.api.domain.member.repository.MemberRepository;
import com.friendzoo.api.domain.order.controller.OrderController;
import com.friendzoo.api.domain.order.dto.OrderHistDTO;
import com.friendzoo.api.domain.order.dto.OrderItemDTO;
import com.friendzoo.api.domain.order.entity.Order;
import com.friendzoo.api.domain.order.entity.OrderItem;
import com.friendzoo.api.domain.order.repository.OrderRepository;
import com.friendzoo.api.domain.order.repository.querydsl.OrderQuerydslRepository;
import com.friendzoo.api.domain.product.entity.Product;
import com.friendzoo.api.domain.product.entity.ProductImage;
import com.friendzoo.api.domain.product.repository.ProductRepository;
import com.friendzoo.api.dto.PageResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Slf4j
@Transactional
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderQuerydslRepository orderQuerydslRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;

    @Override
    public Long order(List<CartItemDTO> cartItemDTOs, String email) {

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 없습니다."));

        List<OrderItem> orderItemList = new ArrayList<>();

        for (CartItemDTO cartItemDTO : cartItemDTOs) {
            log.info("cartItemDTO: {}", cartItemDTO);

            Product product = productRepository.findById(cartItemDTO.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("해당 상품이 없습니다."));


            OrderItem orderItem = OrderItem.createOrderItem(product, cartItemDTO.getQty());
            orderItemList.add(orderItem);
        }
        // 주문 생성
        Order savedOrder = orderRepository.save(Order.createOrder(member, orderItemList));

        return savedOrder.getId();
    }

    @Override
    public List<OrderHistDTO> getOrders(String email) {
        log.info("getOrders email: {}", email);

        List<Order> orders = orderRepository.findByEmail(email);

        List<OrderHistDTO> orderHistDTOs = new ArrayList<>();

        // 주문 내역 조회 orderHistDTOs 에 담기
        for (Order order : orders) {
            OrderHistDTO orderHistDTO = OrderHistDTO.from(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                Product product = productRepository.findById(orderItem.getProduct().getId())
                        .orElseThrow(() -> new EntityNotFoundException("해당 상품이 없습니다."));
                List<ProductImage> imageList = product.getImageList();

                // imageList 내 ord 적은 순으로 정렬 그리고 first image 으로 뽑기
                ProductImage productImage = imageList.stream().min(Comparator.comparing(ProductImage::getOrd))
                        .orElseGet(ProductImage::new);

                OrderItemDTO orderItemDTO = OrderItemDTO.from(orderItem, productImage.getImageName());
                orderHistDTO.addOrderItemDto(orderItemDTO);
            }

            orderHistDTOs.add(orderHistDTO);
        }
        return orderHistDTOs;
    }


    @Override
    public PageResponseDTO<OrderHistDTO> getOrdersPage(String email, OrderController.OrderHisRequestDTO pageRequestDTO) {
        log.info("getOrdersPage ....");

        Pageable pageable = PageRequest.of(
                pageRequestDTO.getPage() - 1,  //페이지 시작 번호가 0부터 시작하므로
                pageRequestDTO.getSize(),
                "asc".equals(pageRequestDTO.getSort()) ?  // 정렬 조건
                        Sort.by("id").ascending() : Sort.by("id").descending()
        );

        log.info("pageable: {}", pageable);

        Page<Order> result = orderQuerydslRepository.findBySearch(email, pageable, pageRequestDTO.getSearchTerm(), pageRequestDTO.getYear());

        List<OrderHistDTO> dtoList = new ArrayList<>();

        for (Order order : result) {
            OrderHistDTO orderHistDTO = OrderHistDTO.from(order);
            List<OrderItem> orderItems = order.getOrderItems();
            for (OrderItem orderItem : orderItems) {
                Product product = productRepository.findById(orderItem.getProduct().getId())
                        .orElseThrow(() -> new EntityNotFoundException("해당 상품이 없습니다."));
                List<ProductImage> imageList = product.getImageList();

                // imageList 내 ord 적은 순으로 정렬 그리고 first image 으로 뽑기
                ProductImage productImage = imageList.stream().min(Comparator.comparing(ProductImage::getOrd))
                        .orElseGet(ProductImage::new);

                OrderItemDTO orderItemDTO = OrderItemDTO.from(orderItem, productImage.getImageName());
                orderHistDTO.addOrderItemDto(orderItemDTO);
            }
            dtoList.add(orderHistDTO);
        }

        long totalCount = result.getTotalElements();

        return PageResponseDTO.<OrderHistDTO>withAll()
                .dtoList(dtoList)
                .totalCount(totalCount)
                .pageRequestDTO(pageRequestDTO)
                .build();
    }


    @Override
    public void validateOrder(Long orderId, String email) {

        Order order = findOrder(orderId);
        // 현재 맴버
        Member curMember = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("해당 회원이 없습니다."));

        // 주문했던 맴버
        Member orderedMember = order.getMember();
        if (!StringUtils.equals(curMember.getEmail(), orderedMember.getEmail())) {
            throw new IllegalArgumentException("해당 주문한 고객이 아닙니다!");
        }
    }


    @Override
    public void cancelOrder(Long orderId, String email) {
        Order order = findOrder(orderId);
        // order 취소
        order.cancelOrder();
    }

    /**
     * 주문 조회
     *
     * @param orderId 주문 ID
     * @return 주문
     */
    private Order findOrder(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("해당 주문이 없습니다."));
    }
}
