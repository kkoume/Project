package kr.co.farmstory.repository;

import kr.co.farmstory.entity.OrderItems;
import kr.co.farmstory.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrdersItemsRepository extends JpaRepository<OrderItems, Integer> {
    public List<OrderItems> findAllByOrderNo(int orderNo);
}
