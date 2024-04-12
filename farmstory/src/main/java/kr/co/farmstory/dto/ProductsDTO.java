package kr.co.farmstory.dto;

import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class ProductsDTO {
    private int prodNo;
    private int cateNo;
    private String prodName;
    private int prodPrice;
    private int prodStock;
    private int prodSold; //판매액
    private int prodDiscount;

    private MultipartFile multImage1;
    private MultipartFile multImage2;
    private MultipartFile multImage3;

    private String image1;
    private String image2;
    private String image3;
    private int point;
    private String etc;
    private int delivery;
    private LocalDateTime RegProdDate;

    private String cateName;

}
