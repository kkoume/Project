package kr.co.farmstory.dto;

import jakarta.mail.Multipart;
import kr.co.farmstory.entity.Article;
import kr.co.farmstory.entity.File;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ArticleDTO {


    private int no;
    private int parent;
    private int comment;
    private String cate;
    private String title;
    private String content;
    private int file;
    private int hit;
    private String writer;
    private String regip;
    private LocalDateTime rdate;

    private List<MultipartFile> files;

    private UserDTO user;
    private List<FileDTO> fileList;

    private String nick;

    public Article toEntity() {
        Article article = new Article();
        article.setNo(no);
        article.setParent(parent);
        article.setComment(comment);
        article.setCate(cate);
        article.setTitle(title);
        article.setContent(content);
        article.setFile(file);
        article.setHit(hit);
        article.setWriter(writer);
        article.setRegip(regip);
        article.setRdate(rdate);
        article.setNick(nick);

        return article;
    }
}