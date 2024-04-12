package kr.co.farmstory.config;

import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class QueryDslConfig {

    /*
        build.gradle 설정 후 JPAQueryFactory 빈 선정을 해야 에러가 나지 않음
     */

    @PersistenceContext
    private EntityManager entityManager;

    @Bean
    public JPAQueryFactory JPAQueryFactory(){
        return new JPAQueryFactory(entityManager);
    }

}
