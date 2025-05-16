package com.seong.portfolio.quiz_quest.user.service.user.xp;


import org.springframework.stereotype.Component;


@Component
public class UserXpImpl implements UserXp {

    public int isLevelUpReturnXp(int xp) {
        if(xp >= 100) {
            xp -= 100;
            return xp;
        }else{
            return -1;
        }
    }

    @Override
    public boolean isFirstProblemSolving(int problemCount) {
        return problemCount == 0;
    }
}
