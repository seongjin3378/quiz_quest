package com.seong.portfolio.quiz_quest.user.repo;

import com.seong.portfolio.quiz_quest.user.vo.UserVO;
import lombok.RequiredArgsConstructor;
import org.apache.ibatis.annotations.Mapper;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Repository;

@Mapper
public interface UserRepository {
   int save(UserVO vo);
   int existsByUserId(UserVO vo);
   UserVO findByUserId(String userId);

}
