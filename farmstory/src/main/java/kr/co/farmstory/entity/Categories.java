package kr.co.farmstory.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@ToString
@Builder
@Entity
@Table(name="categories")

public class Categories {
    @Id
    private int cateNo;
    private  String cateName;
}
