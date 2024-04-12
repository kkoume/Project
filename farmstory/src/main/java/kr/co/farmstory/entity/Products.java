package kr.co.farmstory.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

import java.sql.Blob;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
@Entity
@Table(name="products")

public class Products {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int prodNo;
    private int cateNo;
    private String prodName;
    private int prodPrice;
    private int prodStock;
    private int prodSold; //판매액
    private int prodDiscount;
    private String image1;
    private String image2;
    private String image3;
    private int point;
    private String etc;
    private int delivery;

    @CreationTimestamp
    private LocalDateTime RegProdDate;

    @Transient
    private String cateName;

}
