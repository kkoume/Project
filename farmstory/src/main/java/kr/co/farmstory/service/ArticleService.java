package kr.co.farmstory.service;

import com.querydsl.core.Tuple;
import jakarta.transaction.Transactional;
import kr.co.farmstory.dto.*;
import kr.co.farmstory.entity.Article;
import kr.co.farmstory.entity.Config;
import kr.co.farmstory.entity.File;
import kr.co.farmstory.repository.ArticleRepository;
import kr.co.farmstory.repository.ConfigRepository;
import kr.co.farmstory.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class ArticleService {

    private final ArticleRepository articleRepository;
    private final ConfigRepository configRepository;
    private final FileService fileService;
    private final FileRepository fileRepository;
    private final ModelMapper modelMapper;

    //카테고리 검색(카테고리에 해당하는 Config 엔티티를 찾아 반환)
    public Config findCate(String cate){
        return  configRepository.findById(cate).get();
    }

    // 해당 카테고리의 게시글 목록을 가져옴
    public List<ArticleDTO> getArticlesByCategory(String cate) {
        List<Article> articles = articleRepository.findByCate(cate);

        // Entity를 DTO로 변환하여 반환한다.
        return articles.stream()
                .map(article -> {
                    ArticleDTO dto = new ArticleDTO();
                    dto.setNo(article.getNo());
                    dto.setParent(article.getParent());
                    dto.setComment(article.getComment());
                    dto.setCate(article.getCate());
                    dto.setTitle(article.getTitle());
                    dto.setContent(article.getContent());
                    dto.setFile(article.getFile());
                    dto.setHit(article.getHit());
                    dto.setWriter(article.getWriter());
                    dto.setRegip(article.getRegip());
                    dto.setRdate(article.getRdate());
                    dto.setNick(article.getNick());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    public ArticlePageResponseDTO selectArticles(ArticlePageRequestDTO pageRequestDTO){

        log.info("selectArticles...1");
        Pageable pageable = pageRequestDTO.getPageable("no");

        log.info("selectArticles...2");
        Page<Tuple> pageArticle = articleRepository.selectArticles(pageRequestDTO, pageable);

        log.info("selectArticles...3 : " + pageArticle);
        List<ArticleDTO> dtoList = pageArticle.getContent().stream()
                .map(tuple ->
                        {
                            log.info("tuple : " + tuple);
                            Article article = tuple.get(0, Article.class);
                            String nick = tuple.get(1, String.class);
                            article.setNick(nick);

                            log.info("article : " + article);

                            return modelMapper.map(article, ArticleDTO.class);
                        }
                )
                .toList();

        log.info("selectArticles...4 : " + dtoList);

        int total = (int) pageArticle.getTotalElements();

        return ArticlePageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public ArticlePageResponseDTO searchArticles(ArticlePageRequestDTO pageRequestDTO){

        Pageable pageable = pageRequestDTO.getPageable("no");
        Page<Tuple> pageArticle = articleRepository.searchArticles(pageRequestDTO, pageable);

        List<ArticleDTO> dtoList = pageArticle.getContent().stream()
                .map(tuple ->
                        {
                            log.info("tuple : " + tuple);
                            Article article = tuple.get(0, Article.class);
                            String nick = tuple.get(1, String.class);
                            article.setNick(nick);

                            log.info("article : " + article);

                            return modelMapper.map(article, ArticleDTO.class);
                        }
                )
                .toList();

        int total = (int) pageArticle.getTotalElements();

        return ArticlePageResponseDTO.builder()
                .pageRequestDTO(pageRequestDTO)
                .dtoList(dtoList)
                .total(total)
                .build();
    }

    public ArticleDTO findById(int no){

        Optional<Article> optArticle = articleRepository.findById(no);

        ArticleDTO articleDTO = null;

        if(optArticle.isPresent()){

            Article article = optArticle.get();
            articleDTO = modelMapper.map(article, ArticleDTO.class);
        }
        return articleDTO;
    }

    public void insertArticle(ArticleDTO articleDTO){
        articleDTO.setFile(articleDTO.getFiles().size());

        for(MultipartFile mf : articleDTO.getFiles()){
            if(mf.getOriginalFilename() ==null || mf.getOriginalFilename() == ""){
                articleDTO.setFile(0);
            }
        }
        Article article = modelMapper.map(articleDTO, Article.class);
        log.info(article.toString());
        Article savedArticle= articleRepository.save(article);
        int ano = savedArticle.getNo();
        articleDTO.setNo(ano);

        fileService.fileUpload(articleDTO);
    }

    public ArticleDTO selectArticle(int no){
        return modelMapper.map(articleRepository.findById(no), ArticleDTO.class);
    }

    public void modifyArticle(ArticleDTO articleDTO){
        Article oArticle = articleRepository.findById(articleDTO.getNo()).get();
        ArticleDTO oArticleDTO = modelMapper.map(oArticle, ArticleDTO.class);

        oArticleDTO.setContent(articleDTO.getContent());
        oArticleDTO.setTitle(articleDTO.getTitle());
        oArticleDTO.setFiles(articleDTO.getFiles());

        int count = fileService.fileUpload(oArticleDTO);

        oArticleDTO.setFile(oArticleDTO.getFile()+count);

        Article article = modelMapper.map(oArticleDTO, Article.class);
        articleRepository.save(article);
    }

    // hit 증가
    public ArticleDTO updateHit (ArticleDTO articleDTO){
        Article article = modelMapper.map(articleDTO, Article.class);
        Article article1 = articleRepository.save(article);
        return modelMapper.map(article1, ArticleDTO.class);
    }

    @Transactional
    public void deleteArticle (int no){
        articleRepository.deleteById(no);
        articleRepository.deleteArticlesByParent(no);
    }

    //comment
    public ResponseEntity insertComment(ArticleDTO articleDTO){
        Article savedArticle = articleRepository.save(modelMapper.map(articleDTO, Article.class));

        ArticleDTO savedArticleDTO = modelMapper.map(savedArticle, ArticleDTO.class);

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("comment", savedArticleDTO);

        return ResponseEntity.ok().body(response);
    }

    public List<ArticleDTO> selectComment(int parent){
        List<Tuple> lists =articleRepository.selectComments(parent);

        return lists.stream()
                .map(tuple -> {
                    Article article = tuple.get(0 ,Article.class);
                    String nick = tuple.get(1, String.class);
                    article.setNick(nick);
                    return modelMapper.map(article, ArticleDTO.class);
                })
                .toList();
    }

    public ResponseEntity deleteComment(int no){
        articleRepository.deleteById(no);

        Map<String, Object> map = new HashMap<>();
        map.put("success", true);

        return ResponseEntity.ok().body(map);
    }

    public ArticleDTO selectCommentByNo(int no){
        return modelMapper.map(articleRepository.findById(no), ArticleDTO.class) ;
    }

    public ResponseEntity updateComment(ArticleDTO commentDTO){
        Article article= articleRepository.save(modelMapper.map(commentDTO, Article.class));
        Map<String, Object> map = new HashMap<>();
        map.put("article", article);
        return  ResponseEntity.ok().body(map);
    }





}
