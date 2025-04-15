package com.seong.portfolio.quiz_quest.user.repo;

import com.seong.portfolio.quiz_quest.user.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface UserRepository {
   int save(UserVO vo);

   @Update("UPDATE users SET xp = xp + #{xp} WHERE user_id = #{userId}")
   void increaseXpByUserId(@Param("userId") String userId, @Param("xp") int xp);

   @Update("UPDATE users SET xp = #{xp} WHERE user_id = #{userId}")
   void updateXpByUserId(@Param("userId") String userId, @Param("xp") int xp);

   @Update("UPDATE users SET level = level + #{level} WHERE user_id = #{userId}")
   void increaseLevelByUserId(@Param("userId") String userId, @Param("level") int level);

   /*@Select("SELECT level, xp FROM Users WHERE user_id = #{userId}")*/
   UserVO findLevelAndXpByUserId(@Param("userId") String userId);
   int existsByUserId(UserVO vo);

   UserVO findByUserId(String userId);

   String findUserIdByRefreshToken(String refreshToken);

   List<String> findAllUserId();

   void updateRefreshTokenByUserId(@Param("userId") String userId, @Param("refreshToken") String refreshToken);
}
