package com.seong.portfolio.quiz_quest.user.vo;

import lombok.*;

@Getter
@Setter
@Builder
public class UserVO {
    private String userId;
    private String password;
    private String email;
    private String role;
    private Integer xp;
    private Integer level;
    private String refreshToken;
}
