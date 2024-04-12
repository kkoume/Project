package kr.co.farmstory.aspect;

import kr.co.farmstory.entity.Config;
import kr.co.farmstory.repository.ConfigRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;

import java.util.Optional;

@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class BoardNameAspect {

    private final ConfigRepository configRepository;

    /*
        - 컨트롤러의 각 메서드에 공통 로직 처리하기 위한 Asepct 모듈
        - ArticleController의 메서드 중에서 첫번째 매개변수  model, 두번째 매개변수 cate 인 모든 메서드에 실행
        - 해당 포인트컷 메서드가 리턴 직후에 해당 어드바이스 메서드(addBoardName)가 실행
     */
    @Pointcut("execution(* kr.co.farmstory.controller.ArticleController.*(..))")
    public void boardNameAttribute(){}

    @AfterReturning(pointcut = "boardNameAttribute() && args(model, cate, ..)")
    public void addBoardName(Model model, String cate){
        log.info("addBoardName!!!");
        Optional<Config> optConfig = configRepository.findById(cate);
        optConfig.ifPresent(config -> {
            String boardName = config.getBoardName();
            if (boardName != null && !boardName.isEmpty()) {
                model.addAttribute("boardName", boardName);
            }
        });
    }
}