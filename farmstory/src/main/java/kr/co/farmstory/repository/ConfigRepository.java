package kr.co.farmstory.repository;

import kr.co.farmstory.entity.Config;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ConfigRepository extends JpaRepository<Config, String> {
}
