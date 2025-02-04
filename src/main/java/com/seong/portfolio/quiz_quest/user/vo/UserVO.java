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
    private int xp;
    private int level;
}
