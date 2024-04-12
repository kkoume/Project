package kr.co.farmstory.dto;

import kr.co.farmstory.entity.User;
import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class UserPageResponseDTO {

    private List<UserDTO> dtoList;
    private int pg;
    private int size;
    private int total;
    private int startNo;
    private int start, end;
    private boolean prev, next;

    @Builder
    public UserPageResponseDTO(UserPageRequestDTO userPageRequestDTO, List<UserDTO> dtoList, int total){
        this.pg = userPageRequestDTO.getPg();
        this.size = userPageRequestDTO.getSize();
        this.total = total;
        this.dtoList = dtoList;

        this.startNo = total - ((pg - 1) * size);
        this.end = (int) (Math.ceil(this.pg / 10.0)) * 10;
        this.start = this.end - 9;

        int last = (int) (Math.ceil(total / (double) size));
        this.end = end > last ? last : end;
        this.prev = this.start > 1;
        this.next = total > this.end * this.size;
    }
}