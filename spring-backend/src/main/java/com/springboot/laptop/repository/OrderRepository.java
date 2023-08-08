package com.springboot.laptop.repository;

import com.springboot.laptop.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    // note use nativeQuery
    @Query(value = "SELECT COUNT(*) FROM orders WHERE orderStatus = ?1", nativeQuery = true)
    Integer countNewOrders(int orderStatus);



    /*
    current_date - built-in function
     */
    @Query(value="SELECT COUNT(*) FROM orders o where o.orderStatus in (2,4) and  cast(o.orderDate as date) = cast(current_date as date)", nativeQuery = true)
    Integer countRejectAndCancelToday();

    /*
    doanh thu theo tuần
     */
    @Query(value= "SELECT SUM(o.total) FROM orders o \n" +
            "WHERE o.orderStatus = 3 \n" +
            "AND WEEK(o.orderDate) = WEEK(CURRENT_DATE());", nativeQuery = true)
    Float totalRevenueThisWeek();

    /*
    doanh thu theo ngày
     */
    @Query(value= "\n" +
            "SELECT SUM(total) FROM orders WHERE orderStatus = 3  AND cast(orderDate as date) = cast(current_date as date)", nativeQuery = true)
    Float totalRevenueToday();

    @Query(nativeQuery = true,
            value = "\n" +
                    "select cate.id, cate.category_name, count(o.total) as total from categories cate left join products p on p.category_id = cate.id left join orderdetails od on od.product_id = p.id \n" +
                    "left join orders o on o.id = od.order_id where cate.enabled = true group by cate.id having sum(o.total) is not null limit 3")
    List<Object[]> getCategoryRevenue();



}
