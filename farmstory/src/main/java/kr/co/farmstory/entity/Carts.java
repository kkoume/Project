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
@Table(name="carts")

public class Carts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int cartNo;
    private String userId;
    private int prodNo;
    private int cartProdCount;

    @CreationTimestamp
    private LocalDateTime cartProdDate;

}
