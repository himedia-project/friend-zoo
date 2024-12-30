package com.friendzoo.api.domain.product.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;

@Getter
@Setter
@Entity
@Table(name = "product_image_list")
public class ProductImageList {
    @NotNull
    @Id
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Size(max = 255)
    @Column(name = "image_name")
    private String imageName;

    @NotNull
    @ColumnDefault("0")
    @Column(name = "ord", nullable = false)
    private Integer ord;

}