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

    @Query(value = "SELECT p.* FROM OrderDetails od INNER JOIN products p ON od.product_id = p.id AND enabled = 1  GROUP BY product_id HAVING COUNT(*) > 3 LIMIT 4", nativeQuery = true)
    List<ProductEntity> findBestSellerProducts();


    @Query(value = "select * from products where enabled = true;", nativeQuery = true)
    List<ProductEntity> getActiveProducts();


}
