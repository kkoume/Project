package kr.co.farmstory.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrdersDTO {
    private int orderNo;
    private String userId;
    private int orderTotalPrice;
    private String orderAddr;
    private int itemDiscount;
    private LocalDateTime orderDate;

    private String receiveName;
    private String receiveHp;
    private String memo;
    private String sendHp;
    private String sendName;
}
