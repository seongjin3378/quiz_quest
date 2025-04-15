package com.seong.portfolio.quiz_quest.problems.problemHistory.repo;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface ProblemHistoryRepository {

    @Insert("INSERT INTO problem_history(problem_id, user_id) VALUES(#{problemId}, #{userId});")
    void save(@Param("problemId") long problemId, @Param("userId") String userId);
    @Select("SELECT CASE WHEN COUNT(*) > 0 THEN 1 ELSE 0 END AS result " +
            "FROM problem_history WHERE problem_id = #{problemId} AND user_id = #{userId};")
    int existsByProblemIdAndUserId(@Param("problemId") long problemId, @Param("userId") String userId);
}
