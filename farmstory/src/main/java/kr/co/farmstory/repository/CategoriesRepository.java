package kr.co.farmstory.repository;

import kr.co.farmstory.entity.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoriesRepository extends JpaRepository<Categories , Integer> {
}
