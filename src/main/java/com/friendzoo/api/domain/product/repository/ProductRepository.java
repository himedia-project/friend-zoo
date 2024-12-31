package com.friendzoo.api.domain.product.repository;

import com.friendzoo.api.domain.product.dto.ProductDTO;
import com.friendzoo.api.domain.product.entity.Category;
import com.friendzoo.api.domain.product.entity.Product;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
//    @EntityGraph(attributePaths = "imageList")
//    @Query("select p from Product p where p.id = :pno")
//    Optional<Product> selectOne(@Param("pno") Long pno);

    @Query("select p from Product p where p.name = :name")
    Optional<Product> findByProduct(@Param("name") String name);

    @Query("select p from Product p where p.best= 'Y'")
    List<Product> findBestProducts();

    @Query("select p from Product p where p.mdPick= 'Y'")
    List<Product> findMdPickProducts();

    @Query("select p from Product p")
    List<Product> findNewProducts(Sort createdAt);

    @Query("select p from Product p where p.name LIKE CONCAT('%',:name,'%')")
    List<Product> findSelectedCategory(@Param("name") String name);
}
