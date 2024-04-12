package kr.co.farmstory.entity;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "config")
public class Config {

    @Id
    private String cate;
    private String boardName;
    private String admin;

    @ColumnDefault("0")
    private int total;

    @CreationTimestamp
    private LocalDateTime createDate;



}