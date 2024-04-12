package kr.co.farmstory.dto;

import lombok.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderNotDetailDTO {
    private int orderNo; //완료
    private int totalCount; //완료
    private int delivery; //완료
    private int totalFee; //완료
    private String productName; //완료
    private String orderName; //완룐
    private LocalDateTime orderDate; //완료
}
