package com.seong.portfolio.quiz_quest.likes.repo;

import com.seong.portfolio.quiz_quest.likes.dto.LikesDTO;
import com.seong.portfolio.quiz_quest.likes.dto.totalLikesInfoDTO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface LikesRepository {
    @Insert("INSERT INTO likes(user_num, board_id, like_count, liked_at, board_type) VALUES(#{userNum}, #{boardId}, #{likeCount}, Now(), #{boardType}) " +
            "ON DUPLICATE KEY UPDATE " +
            "like_count = VALUES(like_count)," +
            "liked_at = VALUES(liked_at);")
    void save(LikesDTO likesDTO);
    @Delete("DELETE FROM likes WHERE board_id = #{boardId} AND board_type = #{boardType} AND user_num = #{userNum}")
    void deleteByUserNumAndBoardIdAndBoardType(LikesDTO likesDTO);

    @Select("SELECT like_count FROM likes WHERE user_num = #{userNum} AND board_id = #{boardId} AND board_type = #{boardType};")
    Integer findCurrentStateByUserNumAndBoardIdAndBoardType(@Param("userNum") long userNum, @Param("boardId") long boardId, @Param("boardType") String boardType);
    totalLikesInfoDTO findTotalLikesInfoByBoardIdAndUserNumAndBoardType(@Param("userNum") long userNum, @Param("boardId") long boardId, @Param("boardType") String boardType);
}
