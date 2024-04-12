package kr.co.farmstory.repository;

import kr.co.farmstory.entity.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Integer> {
    public  Page<Orders> findAll(Pageable pageable);
}
