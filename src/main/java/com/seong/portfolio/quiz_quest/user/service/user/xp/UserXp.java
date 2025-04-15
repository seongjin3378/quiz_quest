package com.seong.portfolio.quiz_quest.user.service.user.xp;


/*
* xpProcess
* - xp가 100이상일 경우 남은 xp 반환
* - 아닐 경우 null 반환*/

public interface UserXp {
    int isLevelUpReturnXp(int xp);
    boolean isFirstProblemSolving(int problemCount);
}