package com.friendzoo.domain.product.entity;

import com.friendzoo.domain.product.enums.ProductBest;
import com.friendzoo.domain.product.enums.ProductMdPick;
import com.friendzoo.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;

import java.time.Instant;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(max = 255)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "discount_price")
    @ColumnDefault("0")
    private Integer discountPrice;

    @Lob
    @Column(name = "description")
    private String description;

    @ColumnDefault("0")
    @Column(name = "del_flag")
    private Boolean delFlag;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "stock_number", nullable = false)
    private Integer stockNumber;

    @NotNull
    @Column(name = "best", nullable = false)
    private ProductBest best = ProductBest.N;

    @NotNull
    @Column(name = "md_pick", nullable = false)
    private ProductMdPick mdPick = ProductMdPick.N;


}