package kr.co.farmstory.dto;

import lombok.*;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;


@Getter
@Setter
@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class ProductPageRequestDTO {

    @Builder.Default
    private int prodNo =1;

    @Builder.Default
    private int pg=1;

    @Builder.Default
    private int size =12;

    private String cate;

    /* 검색을 위한 type, keyword 선언 */
    private String type;
    private String keyword;

    public Pageable getPageable(String sort){
        return PageRequest.of(this.pg - 1, this.size, Sort.by(sort).descending());
    }

}