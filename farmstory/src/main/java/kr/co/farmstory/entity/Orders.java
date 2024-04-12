package kr.co.farmstory.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Entity
@Table(name="orders")

public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int orderNo;
    private String userId;
    private int orderTotalPrice;
    private String orderAddr;
    private int itemDiscount;
    @CreationTimestamp
    private LocalDateTime orderDate;
    private String receiveName;
    private String receiveHp;
    private String memo;
    private String sendHp;
    private String sendName;
}
