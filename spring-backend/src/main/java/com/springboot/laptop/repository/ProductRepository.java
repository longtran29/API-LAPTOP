package com.springboot.laptop.repository;

import com.springboot.laptop.model.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<ProductEntity, Long> {

    @Query("UPDATE ProductEntity p SET p.enabled=?2 WHERE p.id = ?1")
    @Modifying(clearAutomatically=true)
    public void updateStatus(Long id, boolean enabled);


    @Query(value = "SELECT * FROM products WHERE original_price < :price", nativeQuery = true)
    public List<ProductEntity> getProductsWithMaxPrice(Long price);


    @Query(value = "SELECT * FROM products p WHERE p.name LIKE %:name%", nativeQuery = true)
    List<ProductEntity> getProductByName(String name);

    @Query(value = "SELECT products.* " +
            "FROM products " +
            "INNER JOIN order_detail ON products.id = order_detail.product_id " +
            "GROUP BY products.id " +
            "ORDER BY COUNT(order_detail.product_id) DESC " +
            "LIMIT 6;", nativeQuery = true)
    List<ProductEntity> findBestSellerProducts();



    @Query(value ="select * from products inner join categories ON categories.id = products.category_id " +
            "where products.enabled = true and categories.enabled = true", nativeQuery = true)
    List<ProductEntity> getActiveProducts();


}
