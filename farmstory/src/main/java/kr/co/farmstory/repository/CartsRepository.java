package kr.co.farmstory.repository;

import kr.co.farmstory.dto.CartsDTO;
import kr.co.farmstory.entity.Carts;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartsRepository extends JpaRepository<Carts, Integer> {
    public List<Carts> findAllByUserId(String uid);
}
