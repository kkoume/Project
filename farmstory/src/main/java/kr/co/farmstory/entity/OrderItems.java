package kr.co.farmstory.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Entity
@Table(name="orderitems")

public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int itemNo;
    private int orderNo;
    private int prodNo;
    private int itemPrice;
    private int itemDiscount;
    private int itemCount;
}
