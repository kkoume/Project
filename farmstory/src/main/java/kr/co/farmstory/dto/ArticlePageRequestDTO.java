package kr.co.farmstory.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class ArticlePageRequestDTO {       // 페이지 요청 정보를 정달하기 위한 클래스

    @Builder.Default                // @Builder.Default : 기본값 설정
    private int no = 1;             // 페이지 요청의 번호

    @Builder.Default
    private int pg = 1;             // 요청된 페이지 번호

    @Builder.Default
    private int size = 10;          // 페이지당 항목 수(한 페이지에 표시할 수)

    private String cate;            // 카테고리

    private String type;
    private String keyword;

    public Pageable getPageable(String sort){
        return PageRequest.of(this.pg - 1, this.size, Sort.by(sort).descending());
    }

}
