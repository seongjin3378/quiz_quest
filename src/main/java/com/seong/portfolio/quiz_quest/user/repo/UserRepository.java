package com.seong.portfolio.quiz_quest.user.repo;

import com.seong.portfolio.quiz_quest.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface UserRepository {
   int save(UserVO vo);
   int existsByUserId(UserVO vo);
   UserVO findByUserId(String userId);
   String findUserIdByRefreshToken(String refreshToken);
   List<String> findAllUserId();
   void updateRefreshTokenByUserId(@Param("userId") String userId, @Param("refreshToken") String refreshToken);
}
