package kr.co.farmstory.repository;

import kr.co.farmstory.entity.Points;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PointsRepository extends JpaRepository<Points, Integer> {
    public List<Points> findAllByUserId(String uid);

    public List<Points> findAllByPointDate(LocalDateTime time);
}
