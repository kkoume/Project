package kr.co.farmstory.entity;

import jakarta.persistence.*;
import kr.co.farmstory.dto.ArticleDTO;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.Formula;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "article")
public class Article {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int no;

    @Builder.Default
    private int parent = 0;

    @Builder.Default
    private int comment = 0;

    private String cate;

    private String title;
    private String content;
    private String writer;

    @Builder.Default
    private int file = 0;

    @Builder.Default
    private int hit = 0;

    private String regip;

    @CreationTimestamp
    private LocalDateTime rdate;

    @OneToMany(mappedBy = "ano") // mappedBy는 매핑 되는 엔티티(테이블)의 FK 컬럼 지정
    private List<File> fileList;

    private String nick;

    public ArticleDTO toDTO() {
        return ArticleDTO.builder()
                .no(this.no)
                .title(this.title)
                .nick(this.nick)
                .rdate(this.rdate)
                .hit(this.hit)
                .build();
    }


}