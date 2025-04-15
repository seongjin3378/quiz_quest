package com.seong.portfolio.quiz_quest.user.service.principalDetails.service;


import com.seong.portfolio.quiz_quest.user.repo.UserRepository;
import com.seong.portfolio.quiz_quest.user.service.principalDetails.vo.PrincipalDetails;
import com.seong.portfolio.quiz_quest.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;




@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private static final Logger logger = LoggerFactory.getLogger(CustomUserDetailsService.class);
    @Override
    public PrincipalDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserVO vo = userRepository.findByUserId(username);
        logger.info("userName: {}", username);
        if(vo != null) {
            logger.info("user id {}", vo.getUserId());
            return new PrincipalDetails(vo);
        }
        logger.info("vo: {}", vo);
        return null;
    }
}
