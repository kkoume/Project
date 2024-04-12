package kr.co.farmstory.repository;

import kr.co.farmstory.entity.Article;
import kr.co.farmstory.entity.ProdImage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdImageRepository extends JpaRepository<ProdImage, Integer> {

    List<ProdImage> findBypNo(int pNo);
}