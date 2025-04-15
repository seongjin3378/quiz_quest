package com.seong.portfolio.quiz_quest.user.vo;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor

public class UserVO {

    private String userId;
    private String password;
    private String email;
    private String role;
    private int xp;
    private int level;
    private String refreshToken;
    private long userNum;
    // 기본 생성자


    // 모든 필드를 포함하는 생성자
}
