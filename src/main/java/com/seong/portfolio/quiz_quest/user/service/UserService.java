package com.seong.portfolio.quiz_quest.user.service;

import com.seong.portfolio.quiz_quest.user.vo.UserVO;

import java.util.Set;

public interface UserService {
    void joinProcess(UserVO vo);
    int getAllActiveUsers();
    void setActiveUserWithExpiry();
    void deleteActiveUsers( Set<String> keys );
}
