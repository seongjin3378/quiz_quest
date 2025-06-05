package com.seong.portfolio.quiz_quest.courses.info.courseLikes.repo;

import com.seong.portfolio.quiz_quest.courses.vo.CourseLikesVO;
import com.seong.portfolio.quiz_quest.courses.vo.CourseTotalLikesInfoVO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface CourseLikesRepository {
    @Insert("INSERT INTO course_likes(user_num, course_id, like_count, liked_at) VALUES(#{userNum}, #{courseId}, #{likeCount}, Now()) " +
            "ON DUPLICATE KEY UPDATE " +
            "like_count = VALUES(like_count)," +
            "liked_at = VALUES(liked_at);")
    void save(CourseLikesVO courseLikesVO);
    @Delete("DELETE FROM course_likes WHERE course_id = #{courseId} AND user_num = #{userNum}")
    void deleteByUserNumAndCourseId(CourseLikesVO courseLikesVO);

    CourseTotalLikesInfoVO findTotalLikesInfoByCourseIdAndUserNum(@Param("userNum") long userNum, @Param("courseId") long courseId);
}
