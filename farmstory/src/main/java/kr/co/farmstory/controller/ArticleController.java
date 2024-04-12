package kr.co.farmstory.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import kr.co.farmstory.dto.ArticleDTO;
import kr.co.farmstory.dto.ArticlePageRequestDTO;
import kr.co.farmstory.dto.ArticlePageResponseDTO;
import kr.co.farmstory.entity.Config;
import kr.co.farmstory.service.ArticleService;
import kr.co.farmstory.service.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.http.HttpRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Controller
@Slf4j
public class ArticleController {

    private final ArticleService articleService;
    private final FileService fileService;

    // 팜스토리 소개 (32 ~ 58)--------------------------------------------------------------------------------------------
    // 인사말
    @GetMapping("/introduction/hello")
    public String introductionHello(@RequestParam("cate") String cate, Model model) {

        List<ArticleDTO> articles = articleService.getArticlesByCategory(cate);
        model.addAttribute("articles", articles);

        Config cateName = articleService.findCate(cate);
        model.addAttribute("cateName", cateName);

        return "/introduction/hello";
    }

    // 찾아오시는길
    @GetMapping("/introduction/direction")
    public String introductionDirection(@RequestParam("cate") String cate, Model model) {
        List<ArticleDTO> articles = articleService.getArticlesByCategory(cate);

        model.addAttribute("articles", articles);
        Config cateName = articleService.findCate(cate);

        model.addAttribute("cateName", cateName);

        return "/introduction/direction";
    }


    // croptalk (60 ~ 213)----------------------------------------------------------------------------------------------
    // 리스트
    @GetMapping("/croptalk/list")
    public String croptalkList(@RequestParam("cate") String cate, Model model,
                               ArticlePageRequestDTO pageRequestDTO, @RequestParam(value = "pg", required = false) Integer pg) {

        ArticlePageResponseDTO pageResponseDTO;

        if(pageRequestDTO.getType() == null){
            if(pg != null){
                pageRequestDTO.setPg(pg);
            }
            // 일반 글 목록 조회
            pageResponseDTO = articleService.selectArticles(pageRequestDTO);
        }else{
            if(pg != null){
                pageRequestDTO.setPg(pg);
            }
            // 검색 글 목록 조회
            pageResponseDTO = articleService.searchArticles(pageRequestDTO);
            model.addAttribute("keyword", pageRequestDTO.getKeyword());
            model.addAttribute("type", pageRequestDTO.getType());
        }
        model.addAttribute(pageResponseDTO);

        List<ArticleDTO> articles = articleService.getArticlesByCategory(cate);
        model.addAttribute("articles", articles);

        Config cateName = articleService.findCate(cate);
        model.addAttribute("cateName", cateName);

        return "/croptalk/list";
    }

    // 글쓰기(폼)
    @GetMapping("/croptalk/write")
    public String croptalkWriteForm(@RequestParam("cate") String cate, Model model, HttpSession session) {

        String nick = (String) session.getAttribute("nick");
        model.addAttribute("nick", nick);

        Config cateName = articleService.findCate(cate);
        model.addAttribute("cateName", cateName);

        return "/croptalk/write";
    }

    // 글쓰기(기능)
    @PostMapping("/croptalk/write")
    public String croptalkWrite(@ModelAttribute ArticleDTO articleDTO, HttpServletRequest request) {

        articleDTO.setRegip(request.getRemoteAddr());
        articleService.insertArticle(articleDTO);

        log.info(articleDTO.toString());

        return "redirect:/croptalk/list?cate=" + articleDTO.getCate();
    }

    // 글보기
    @GetMapping("/croptalk/view")
    public String croptalkView(@RequestParam("cate") String cate, Model model, @RequestParam("no") int no) {

        ArticleDTO articleDTO = articleService.findById(no);
        Config cateName = articleService.findCate(cate);

        if (articleDTO != null) {
            model.addAttribute("articleDTO", articleDTO);
        }

        model.addAttribute("cateName", cateName);

        articleDTO.setHit(articleDTO.getHit() +1);
        articleService.updateHit(articleDTO);

        List<ArticleDTO> comments = articleService.selectComment(no);
        log.info("comments "+comments);
        model.addAttribute("cate", articleDTO.getCate());
        model.addAttribute("comments",comments);

        return "/croptalk/view";
    }

    // 글수정(폼)
    @GetMapping("/croptalk/modify")
    public String croptalkModifyFrom(int no, @RequestParam("cate") String cate,  Model model){

        ArticleDTO articleDTO = articleService.selectArticle(no);
        model.addAttribute("article", articleDTO);

        Config cateName = articleService.findCate(cate);
        model.addAttribute("cateName", cateName);

        model.addAttribute("cate", articleDTO.getCate());

        return "/croptalk/modify";
    }

    // 글수정(기능)
    @PostMapping("/croptalk/modify")
    public String croptalkModify(ArticleDTO articleDTO) {

        articleService.modifyArticle(articleDTO);

        return "redirect:/croptalk/view?no=" + articleDTO.getNo()+"&cate="+articleDTO.getCate();
    }

    // 글삭제(기능)
    @GetMapping("/croptalk/delete")
    public String croptalkDelete(int no, String cate) {
        fileService.deleteFiles(no);
        articleService.deleteArticle(no);

        return "redirect:/croptalk/list?cate=" + cate;
    }

    // 댓글작성(기능) - 동적
    @PostMapping("/croptalk/insertComment")
    public ResponseEntity croptalkInsertComment(@RequestBody ArticleDTO commentDTO, HttpServletRequest request){
        commentDTO.setRegip(request.getRemoteAddr());
        log.info("info.. "+commentDTO);
        ResponseEntity responseEntity = articleService.insertComment(commentDTO);
        // 댓글 작성자의 닉네임과 날짜를 함께 반환
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    // 댓글삭제(기능) - 동적
    @ResponseBody
    @DeleteMapping("/croptalk/deleteComment/{no}")
    public ResponseEntity croptalkDeleteComment(@PathVariable("no") int no){
        return   articleService.deleteComment(no);
    }

    // 댓글수정(기능) - 동적
    @ResponseBody
    @PutMapping("/croptalk/modifyComment")
    public ResponseEntity  croptalkModifyComment(@RequestBody ArticleDTO commentDTO){
        log.info("modify! "+commentDTO);
        ArticleDTO oldComment = articleService.selectCommentByNo(commentDTO.getNo());
        oldComment.setContent(commentDTO.getContent());

        return articleService.updateComment(oldComment);
    }

    // 댓글선택(수정할 댓글)
    @ResponseBody
    @GetMapping("/croptalk/selectComment/{no}")
    public ResponseEntity  croptalkSelectComment(@PathVariable("no") int no){
        ArticleDTO articleDTO =articleService.selectCommentByNo(no);
        Map<String , Object> map = new HashMap<>();
        map.put("article", articleDTO);
        return ResponseEntity.ok().body(map);
    }


    // event (214 ~ 369)------------------------------------------------------------------------------------------------
    // 리스트
    @GetMapping("/event/list")
    public String eventList(@RequestParam("cate") String cate, Model model,
                            ArticlePageRequestDTO pageRequestDTO, @RequestParam(value = "pg", required = false) Integer pg) {

        ArticlePageResponseDTO pageResponseDTO;

        if(pageRequestDTO.getType() == null){
            if(pg != null){
                pageRequestDTO.setPg(pg);
            }
            // 일반 글 목록 조회
            pageResponseDTO = articleService.selectArticles(pageRequestDTO);
        }else{
            if(pg != null){
                pageRequestDTO.setPg(pg);
            }
            // 검색 글 목록 조회
            pageResponseDTO = articleService.searchArticles(pageRequestDTO);
            model.addAttribute("keyword", pageRequestDTO.getKeyword());
            model.addAttribute("type", pageRequestDTO.getType());
        }
        model.addAttribute(pageResponseDTO);

        List<ArticleDTO> articles = articleService.getArticlesByCategory(cate);
        model.addAttribute("articles", articles);

        Config cateName = articleService.findCate(cate);
        model.addAttribute("cateName", cateName);

        return "/event/list";
    }

    // 글쓰기(폼)
    @GetMapping("/event/write")
    public String eventWriteForm(@RequestParam("cate") String cate, Model model, HttpSession session) {

        String nick = (String) session.getAttribute("nick");
        model.addAttribute("nick", nick);

        Config cateName = articleService.findCate(cate);
        model.addAttribute("cateName", cateName);

        return "/event/write";
    }

    // 글쓰기(기능)
    @PostMapping("/event/write")
    public String eventWrite(@ModelAttribute ArticleDTO articleDTO, HttpServletRequest request) {

        articleDTO.setRegip(request.getRemoteAddr());
        articleService.insertArticle(articleDTO);

        log.info(articleDTO.toString());

        return "redirect:/event/list?cate=" + articleDTO.getCate();
    }

    // 글보기
    @GetMapping("/event/view")
    public String eventView(@RequestParam("cate") String cate, Model model, @RequestParam("no") int no) {

        ArticleDTO articleDTO = articleService.findById(no);
        Config cateName = articleService.findCate(cate);

        if (articleDTO != null) {
            model.addAttribute("articleDTO", articleDTO);
        }

        model.addAttribute("cateName", cateName);

        articleDTO.setHit(articleDTO.getHit() +1);
        articleService.updateHit(articleDTO);

        List<ArticleDTO> comments = articleService.selectComment(no);
        log.info("comments "+comments);
        model.addAttribute("cate", articleDTO.getCate());
        model.addAttribute("comments",comments);

        return "/event/view";
    }

    // 글수정(폼)
    @GetMapping("/event/modify")
    public String eventModifyFrom(int no, @RequestParam("cate") String cate,  Model model){

        ArticleDTO articleDTO = articleService.selectArticle(no);
        model.addAttribute("article", articleDTO);

        Config cateName = articleService.findCate(cate);
        model.addAttribute("cateName", cateName);

        model.addAttribute("cate", articleDTO.getCate());

        return "/event/modify";
    }

    // 글수정(기능)
    @PostMapping("/event/modify")
    public String eventModify(ArticleDTO articleDTO) {

        articleService.modifyArticle(articleDTO);

        return "redirect:/event/view?no=" + articleDTO.getNo()+"&cate="+articleDTO.getCate();
    }

    // 글삭제(기능)
    @GetMapping("/event/delete")
    public String eventDelete(int no, String cate) {
        fileService.deleteFiles(no);
        articleService.deleteArticle(no);

        return "redirect:/event/list?cate=" + cate;
    }

    // 댓글작성(기능) - 동적
    @PostMapping("/event/insertComment")
    public ResponseEntity eventInsertComment(@RequestBody ArticleDTO commentDTO, HttpServletRequest request){
        commentDTO.setRegip(request.getRemoteAddr());
        log.info("info.. "+commentDTO);
        ResponseEntity responseEntity = articleService.insertComment(commentDTO);
        // 댓글 작성자의 닉네임과 날짜를 함께 반환
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    // 댓글삭제(기능) - 동적
    @ResponseBody
    @DeleteMapping("/event/deleteComment/{no}")
    public ResponseEntity eventDeleteComment(@PathVariable("no") int no){
        return   articleService.deleteComment(no);
    }

    // 댓글수정(기능) - 동적
    @ResponseBody
    @PutMapping("/event/modifyComment")
    public ResponseEntity  eventModifyComment(@RequestBody ArticleDTO commentDTO){
        log.info("modify! "+commentDTO);
        ArticleDTO oldComment = articleService.selectCommentByNo(commentDTO.getNo());
        oldComment.setContent(commentDTO.getContent());

        return articleService.updateComment(oldComment);
    }

    // 댓글선택(수정할 댓글)
    @ResponseBody
    @GetMapping("/event/selectComment/{no}")
    public ResponseEntity  eventSelectComment(@PathVariable("no") int no){
        ArticleDTO articleDTO =articleService.selectCommentByNo(no);
        Map<String , Object> map = new HashMap<>();
        map.put("article", articleDTO);
        return ResponseEntity.ok().body(map);
    }


    // community (369 ~ 523)--------------------------------------------------------------------------------------------
    // 리스트
    @GetMapping("/community/list")
    public String communityList(@RequestParam("cate") String cate, Model model,
                                ArticlePageRequestDTO pageRequestDTO, @RequestParam(value = "pg", required = false) Integer pg) {

        ArticlePageResponseDTO pageResponseDTO;

        if(pageRequestDTO.getType() == null){
            if(pg != null){
                pageRequestDTO.setPg(pg);
            }
            // 일반 글 목록 조회
            pageResponseDTO = articleService.selectArticles(pageRequestDTO);
        }else{
            if(pg != null){
                pageRequestDTO.setPg(pg);
            }
            // 검색 글 목록 조회
            pageResponseDTO = articleService.searchArticles(pageRequestDTO);
            model.addAttribute("keyword", pageRequestDTO.getKeyword());
            model.addAttribute("type", pageRequestDTO.getType());
        }
        model.addAttribute(pageResponseDTO);

        List<ArticleDTO> articles = articleService.getArticlesByCategory(cate);
        model.addAttribute("articles", articles);

        Config cateName = articleService.findCate(cate);
        model.addAttribute("cateName", cateName);

        return "/community/list";
    }

    // 글쓰기(폼)
    @GetMapping("/community/write")
    public String communityWriteForm(@RequestParam("cate") String cate, Model model, HttpSession session) {

        String nick = (String) session.getAttribute("nick");
        model.addAttribute("nick", nick);

        Config cateName = articleService.findCate(cate);
        model.addAttribute("cateName", cateName);

        return "/community/write";
    }

    // 글쓰기(기능)
    @PostMapping("/community/write")
    public String communityWrite(@ModelAttribute ArticleDTO articleDTO, HttpServletRequest request) {

        articleDTO.setRegip(request.getRemoteAddr());
        articleService.insertArticle(articleDTO);

        log.info(articleDTO.toString());

        return "redirect:/community/list?cate=" + articleDTO.getCate();
    }

    // 글보기
    @GetMapping("/community/view")
    public String communityView(@RequestParam("cate") String cate, Model model, @RequestParam("no") int no) {

        ArticleDTO articleDTO = articleService.findById(no);
        Config cateName = articleService.findCate(cate);

        if (articleDTO != null) {
            model.addAttribute("articleDTO", articleDTO);
        }

        model.addAttribute("cateName", cateName);

        articleDTO.setHit(articleDTO.getHit() +1);
        articleService.updateHit(articleDTO);

        List<ArticleDTO> comments = articleService.selectComment(no);
        log.info("comments "+comments);
        model.addAttribute("cate", articleDTO.getCate());
        model.addAttribute("comments",comments);

        return "/community/view";
    }

    // 글수정(폼)
    @GetMapping("/community/modify")
    public String communityModifyFrom(int no, @RequestParam("cate") String cate,  Model model){

        ArticleDTO articleDTO = articleService.selectArticle(no);
        model.addAttribute("article", articleDTO);

        Config cateName = articleService.findCate(cate);
        model.addAttribute("cateName", cateName);

        model.addAttribute("cate", articleDTO.getCate());

        return "/community/modify";
    }

    // 글수정(기능)
    @PostMapping("/community/modify")
    public String communityModify(ArticleDTO articleDTO) {

        articleService.modifyArticle(articleDTO);

        return "redirect:/community/view?no=" + articleDTO.getNo()+"&cate="+articleDTO.getCate();
    }

    // 글삭제(기능)
    @GetMapping("/community/delete")
    public String communityDelete(int no, String cate) {
        fileService.deleteFiles(no);
        articleService.deleteArticle(no);

        return "redirect:/community/list?cate=" + cate;
    }

    // 댓글작성(기능) - 동적
    @PostMapping("/community/insertComment")
    public ResponseEntity communityInsertComment(@RequestBody ArticleDTO commentDTO, HttpServletRequest request){
        commentDTO.setRegip(request.getRemoteAddr());
        log.info("info.. "+commentDTO);
        ResponseEntity responseEntity = articleService.insertComment(commentDTO);
        // 댓글 작성자의 닉네임과 날짜를 함께 반환
        return new ResponseEntity<>(responseEntity.getBody(), responseEntity.getStatusCode());
    }

    // 댓글삭제(기능) - 동적
    @ResponseBody
    @DeleteMapping("/community/deleteComment/{no}")
    public ResponseEntity communityDeleteComment(@PathVariable("no") int no){
        return   articleService.deleteComment(no);
    }

    // 댓글수정(기능) - 동적
    @ResponseBody
    @PutMapping("/community/modifyComment")
    public ResponseEntity  communityModifyComment(@RequestBody ArticleDTO commentDTO){
        log.info("modify! "+commentDTO);
        ArticleDTO oldComment = articleService.selectCommentByNo(commentDTO.getNo());
        oldComment.setContent(commentDTO.getContent());

        return articleService.updateComment(oldComment);
    }

    // 댓글선택(수정할 댓글)
    @ResponseBody
    @GetMapping("/community/selectComment/{no}")
    public ResponseEntity  communitySelectComment(@PathVariable("no") int no){
        ArticleDTO articleDTO =articleService.selectCommentByNo(no);
        Map<String , Object> map = new HashMap<>();
        map.put("article", articleDTO);
        return ResponseEntity.ok().body(map);
    }
}