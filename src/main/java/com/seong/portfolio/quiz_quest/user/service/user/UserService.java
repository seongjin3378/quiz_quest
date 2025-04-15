package com.seong.portfolio.quiz_quest.user.service.user;

import com.seong.portfolio.quiz_quest.user.vo.UserVO;

public interface UserService {
    void joinProcess(UserVO vo);
    void xpProcess(int xp, long problemId);
}
