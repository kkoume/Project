package kr.co.farmstory.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class CartsDTO {
    private int cartNo;
    private String userId;
    private int prodNo;
    private int cartProdCount;
    private LocalDateTime cartProdDate;
}
