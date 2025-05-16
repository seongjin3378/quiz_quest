package com.seong.portfolio.quiz_quest.user.service.user;

import com.seong.portfolio.quiz_quest.problems.problemHistory.service.ProblemHistoryService;
import com.seong.portfolio.quiz_quest.user.repo.UserRepository;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import com.seong.portfolio.quiz_quest.user.service.user.xp.UserXp;
import com.seong.portfolio.quiz_quest.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final SessionService sessionService;
    private final UserXp userXp;
    @Override
    public void joinProcess(UserVO vo) {
        int isUser = userRepository.existsByUserId(vo);
        if(isUser == 1) {
            return;
        }
        userRepository.save(vo);
    }



    @Override
    @Transactional
    public void xpProcess(int xp, long problemId) {

        String userId = sessionService.getSessionId();
        UserVO vo = userRepository.findLevelAndXpByUserId(userId);

        /*마이페이지 풀었던 문제 번호 조회 만들어야함*/

            log.info("xp Process 실행");

            int totalXP = xp+vo.getXp();
            int leftOverXP = userXp.isLevelUpReturnXp(totalXP);
            if(leftOverXP >= 0) { /* 레벨업 했을 경우 */
                userRepository.increaseLevelByUserId(userId,  1);
                userRepository.updateXpByUserId(userId, 0);
                userRepository.increaseXpByUserId(userId, leftOverXP);
            }else {
                userRepository.increaseXpByUserId(userId, xp);
            }

    }




}
