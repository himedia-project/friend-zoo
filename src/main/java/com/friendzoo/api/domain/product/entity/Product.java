package com.friendzoo.api.domain.product.entity;

import com.friendzoo.api.domain.heart.entity.Heart;
import com.friendzoo.api.domain.product.enums.ProductBest;
import com.friendzoo.api.domain.product.enums.ProductMdPick;
import com.friendzoo.api.domain.test.entity.Test;
import com.friendzoo.api.entity.BaseEntity;
import com.friendzoo.api.exception.OutOfStockException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import kotlin.Lazy;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.util.ArrayList;
import java.util.List;

@SuperBuilder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@ToString(exclude = "imageList")
@Table(name = "product")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "product_id", nullable = false)
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

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'N'")
    private ProductBest best;

    @Enumerated(EnumType.STRING)
    @ColumnDefault("'N'")
    private ProductMdPick mdPick;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @Builder.Default
    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Heart> heartList = new ArrayList<>();

    @ElementCollection
    @Builder.Default
    private List<ProductImage> imageList = new ArrayList<>();


    // 재고수량 감소
    public void removeStock(int stockNumber) {
        int restStock = this.stockNumber - stockNumber;
        if (restStock < 0) {
            throw new OutOfStockException("상품의 재고가 부족합니다. (현재 재고 수량: " + this.stockNumber + ")");
        }
        this.stockNumber = restStock;
    }

    // 재고수량 증가
    public void addStock(int stockNumber) {
        this.stockNumber += stockNumber;
    }

    /**
     * 상품 이미지, 상품에 추가
     *
     * @param image 이미지
     */
    public void addImage(ProductImage image) {

        image.setOrd(this.imageList.size());
        imageList.add(image);
    }


    /**
     * 상품 이미지 파일, 상품이미지에 추가
     *
     * @param fileName 이미지 파일명
     */
    public void addImageString(String fileName) {

        ProductImage productImage = ProductImage.builder()
                .imageName(fileName)
                .build();
        addImage(productImage);

    }

    /**
     * 상품 이미지 리스트 초기화
     */
    public void clearImageList() {
        this.imageList.clear();
    }

    /*
        * 상품 수정 로직
     */
    public void changeName(String name) {
        this.name = name;
    }

    public void changePrice(Integer price) {
        this.price = price;
    }

    public void changeDiscountPrice(Integer discountPrice) {
        this.discountPrice = discountPrice;
    }

    public void changeDescription(String description) {
        this.description = description;
    }

    public void changeStockNumber(Integer stockNumber) {
        this.stockNumber = stockNumber;
    }

    public void changeBest(ProductBest best) {
        this.best = best;
    }

    public void changeMdPick(ProductMdPick mdPick) {
        this.mdPick = mdPick;
    }

    public void changeCategory(Category category) {
        this.category = category;
    }

    public boolean getIsHeart() {
        return !this.heartList.isEmpty();
    }
}