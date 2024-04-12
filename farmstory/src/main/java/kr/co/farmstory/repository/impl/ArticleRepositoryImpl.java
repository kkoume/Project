package kr.co.farmstory.repository.impl;

import com.querydsl.core.QueryResults;
import com.querydsl.core.Tuple;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.co.farmstory.dto.ArticlePageRequestDTO;
import kr.co.farmstory.entity.QArticle;
import kr.co.farmstory.entity.QUser;
import kr.co.farmstory.repository.custum.ArticleRepositoryCustom;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Repository
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {

    private final JPAQueryFactory jpaQueryFactory;
    private QArticle qArticle = QArticle.article;
    private QUser qUser = QUser.user;

    @Override
    public Page<Tuple> selectArticles(ArticlePageRequestDTO pageRequestDTO, Pageable pageable){

        String cate = pageRequestDTO.getCate();

        // 부가적인 Query 실행 정보를 처리하기 위해 fetchResults()로 실행
        QueryResults<Tuple> results = jpaQueryFactory
                                            .select(qArticle, qUser.nick)
                                            .from(qArticle)
                                            .where(qArticle.cate.eq(cate).and(qArticle.parent.eq(0)))
                                            .join(qUser)
                                            .on(qArticle.writer.eq(qUser.uid))
                                            .offset(pageable.getOffset())
                                            .limit(pageable.getPageSize())
                                            .orderBy(qArticle.no.desc())
                                            .fetchResults();

        List<Tuple> content = results.getResults();

        long total = results.getTotal();

        // 페이징 처리를 위해 page 객체 리턴
        return new PageImpl<>(content, pageable, total);
    }


    @Override
    public Page<Tuple> searchArticles(ArticlePageRequestDTO pageRequestDTO, Pageable pageable){

        String cate = pageRequestDTO.getCate();
        String type = pageRequestDTO.getType();
        String keyword = pageRequestDTO.getKeyword();

        BooleanExpression expression = null;

        // 검색 종류에 따른 where 표현식 생성
        if(type.equals("title")) {
            expression = qArticle.cate.eq(cate).and(qArticle.title.contains(keyword));
        }else if(type.equals("content")) {
            expression = qArticle.cate.eq(cate).and(qArticle.content.contains(keyword));
        }else if(type.equals("title_content")) {

            BooleanExpression titleContains = qArticle.title.contains(keyword);
            BooleanExpression contentContains = qArticle.content.contains(keyword);
            expression = qArticle.cate.eq(cate).and(titleContains.or(contentContains));

        }else if(type.equals("writer")) {
            expression = qArticle.cate.eq(cate).and(qArticle.parent.eq(0)).and(qUser.nick.contains(keyword));
        }

        // 부가적인 Query 실행 정보를 처리하기 위해 fetchResults()로 실행
        // select * from article where `cate` ='notice' and `type` contains(k) limit 0,10
        QueryResults<Tuple> results = jpaQueryFactory
                                            .select(qArticle, qUser.nick)
                                            .from(qArticle)
                                            .join(qUser)
                                            .on(qArticle.writer.eq(qUser.uid))
                                            .where(expression)
                                            .offset(pageable.getOffset())
                                            .limit(pageable.getPageSize())
                                            .orderBy(qArticle.no.desc())
                                            .fetchResults();


        List<Tuple> content = results.getResults();
        long total = results.getTotal();


        // 페이징 처리를 위해 page 객체 리턴
        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public List<Tuple> selectComments(int no) {

        //부가적인 Query 실행 정보를 처리하기 위해 fetchResults()로 실행
        QueryResults<Tuple> results = jpaQueryFactory
                .select(qArticle, qUser.nick)
                .from(qArticle)
                .join(qUser)
                .on(qArticle.writer.eq(qUser.uid))
                .where(qArticle.parent.eq(no))
                .orderBy(qArticle.no.desc())
                .fetchResults();

        return results.getResults();
    }
}
