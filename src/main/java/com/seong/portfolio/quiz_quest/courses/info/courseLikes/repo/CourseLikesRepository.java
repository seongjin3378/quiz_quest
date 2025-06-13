package com.seong.portfolio.quiz_quest.courses.info.courseLikes.repo;

import com.seong.portfolio.quiz_quest.courses.vo.CourseLikesVO;
import com.seong.portfolio.quiz_quest.courses.vo.CourseTotalLikesInfoVO;
import org.apache.ibatis.annotations.*;

@Mapper
public interface CourseLikesRepository {
    @Insert("INSERT INTO course_likes(user_num, course_id, like_count, liked_at) VALUES(#{userNum}, #{courseId}, #{likeCount}, Now()) " +
            "ON DUPLICATE KEY UPDATE " +
            "like_count = VALUES(like_count)," +
            "liked_at = VALUES(liked_at);")
    void save(CourseLikesVO courseLikesVO);
    @Delete("DELETE FROM course_likes WHERE course_id = #{courseId} AND user_num = #{userNum}")
    void deleteByUserNumAndCourseId(CourseLikesVO courseLikesVO);

    @Select("SELECT like_count FROM course_likes WHERE user_num = #{userNum} AND course_id = #{courseId};")
    Integer findCurrentStateByUserNumAndCourseId(@Param("userNum") long userNum, @Param("courseId") long courseId);
    CourseTotalLikesInfoVO findTotalLikesInfoByCourseIdAndUserNum(@Param("userNum") long userNum, @Param("courseId") long courseId);
}
