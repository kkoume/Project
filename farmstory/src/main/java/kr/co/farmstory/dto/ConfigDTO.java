package kr.co.farmstory.dto;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ConfigDTO {                // 게시판의 설정 정보를 저장하기 위한 클래스

    private String cate;                // 게시판 카테고리(자유게시판, 질문과답변 등등)
    private String boardName;           // 게시판 이름("자유게시판", "질문과답변" 등등)
    private String admin;               // 게시판 관리자(해당 게시판을 관리하는 사용자의 정보)
    private int total;                  // 게시판에 등록된 총 글 수
    private LocalDateTime createDate;   // 게시판 생성일(날짜 시간)

}