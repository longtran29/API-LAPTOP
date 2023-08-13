package com.springboot.laptop.repository;

import com.springboot.laptop.model.Order;
import com.springboot.laptop.model.UserEntity;
import com.springboot.laptop.model.enums.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    // note use nativeQuery
    @Query(value = "SELECT COUNT(*) FROM orders WHERE order_status = ?1", nativeQuery = true)
    Integer countNewOrders(String orderStatus);


    List<Order> findByUser(UserEntity user);

    /*
    current_date - built-in function
     */
    @Query(value="SELECT COUNT(*) FROM orders o where o.order_status =?1 and  cast(o.order_date as date) = cast(current_date as date)", nativeQuery = true)
    Integer countCancelToday(String orderStatus);

    /*
    doanh thu theo tuần
     */
    @Query(value = "SELECT SUM(o.total) " +
            "FROM orders o " +
            "WHERE o.order_status = 'DELIVERED' " +
            "AND EXTRACT('isoyear' FROM o.order_date) = EXTRACT('isoyear' FROM CURRENT_DATE) " +
            "AND EXTRACT('week' FROM o.order_date) = EXTRACT('week' FROM CURRENT_DATE)",
            nativeQuery = true)
    Float sumTotalForDeliveredOrdersThisWeek();

    /*
    doanh thu theo ngày
     */
    @Query(value= "SELECT SUM(total) FROM orders WHERE order_status= 'DELIVERED'  AND cast(order_date as date) = cast(current_date as date)", nativeQuery = true)
    Float totalRevenueToday();

    @Query(nativeQuery = true,
            value = "select cate.id, cate.category_name, count(o.total) as total from categories cate left join products p on p.category_id = cate.id left join order_detail od on od.product_id = p.id \n" +
                    "left join orders o on o.id = od.order_id where cate.enabled = true group by cate.id having sum(o.total) is not null limit 3")
    List<Object[]> getCategoryRevenue();



}
