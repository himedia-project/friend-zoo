package com.friendzoo.api.domain.product.repository;

import com.friendzoo.api.domain.product.entity.Category;
import com.friendzoo.api.domain.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    @Query("select p from Category p")
    List<Category> findCategory();

    @Query("select p from Category p where p.name = :name")
    List<Category> findSelectedCategory(@Param("name") String name);
}
