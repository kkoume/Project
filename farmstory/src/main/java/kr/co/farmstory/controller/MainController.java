package kr.co.farmstory.controller;

import kr.co.farmstory.repository.ArticleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MainController {
    @Autowired
    private ArticleRepository articleRepository;

    @GetMapping(value = {"/", "/index"})
    public String index(Authentication authentication, Model model){
        // 각 카테고리에 해당하는 최신 게시글 가져오기
        model.addAttribute("grow", articleRepository.findTop5ByCateOrderByRdateDesc("grow"));
        model.addAttribute("school", articleRepository.findTop5ByCateOrderByRdateDesc("school"));
        model.addAttribute("story", articleRepository.findTop5ByCateOrderByRdateDesc("story"));

        // 기타 카테고리에 해당하는 최신 게시글 가져오기
        model.addAttribute("notice", articleRepository.findTop3ByCateOrderByRdateDesc("notice"));
        model.addAttribute("qna", articleRepository.findTop3ByCateOrderByRdateDesc("qna"));
        model.addAttribute("faq", articleRepository.findTop3ByCateOrderByRdateDesc("faq"));

        return "/index";
    }
}
