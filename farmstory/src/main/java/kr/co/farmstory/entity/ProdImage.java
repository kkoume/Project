package kr.co.farmstory.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "prodimage")
public class ProdImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int iNo;
    private int pNo;
    private String oName;
    private String sName;


}