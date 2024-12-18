package com.seong.portfolio.quiz_quest.user.service;

import com.seong.portfolio.quiz_quest.ranking.repo.RankingRepository;
import com.seong.portfolio.quiz_quest.ranking.vo.RankingVO;
import com.seong.portfolio.quiz_quest.user.repo.UserRepository;
import com.seong.portfolio.quiz_quest.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RankingRepository rankingRepository;
    @Override
    public void joinProcess(UserVO vo) {
        int isUser = userRepository.existsByUserId(vo);
        if(isUser == 1) {
            return;
        }
        userRepository.save(vo);
    }


}
