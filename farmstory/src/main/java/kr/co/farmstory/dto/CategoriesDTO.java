package kr.co.farmstory.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class CategoriesDTO {

    private int cateNo;
    private String cateName;

}
