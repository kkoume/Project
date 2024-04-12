package kr.co.farmstory.dto;

import lombok.*;


import java.rmi.registry.LocateRegistry;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
public class UserDTO {

    private String uid;
    private String pass;
    private String name;
    private String nick;
    private String email;
    private String hp;
    private String role;
    private String zip;
    private String addr1;
    private String addr2;
    private String regip;
    private String sms;

    private String provider;
    private String grade;

    private LocalDateTime regDate;
    private LocalDateTime leaveDate;
    private int totalPoint;
    private int totalPrice;

}
