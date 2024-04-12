package kr.co.farmstory.dto;

import lombok.*;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PointsDTO {
    private int pointNo;
    private String userId;
    private int point;
    private String pointDesc;
    private LocalDateTime pointDate;
}
