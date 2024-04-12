package kr.co.farmstory.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class OrderItemsDTO {
    private int itemNo;
    private int orderNo;
    private int prodNo;
    private int itemPrice;
    private int itemDiscount;
    private int itemCount;
}
