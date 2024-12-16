package com.seong.portfolio.quiz_quest.user.vo;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UserVO {
    private String userId;
    private String password;
    private String email;
    private String role;
}
