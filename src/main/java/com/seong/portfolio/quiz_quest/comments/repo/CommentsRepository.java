package com.seong.portfolio.quiz_quest.comments.repo;

import com.seong.portfolio.quiz_quest.comments.dto.CommentsDTO;
import com.seong.portfolio.quiz_quest.comments.dto.CommentsRepoDTO;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface CommentsRepository {
    long save(CommentsDTO commentsDTO);

    @Select("SELECT board_id FROM comments WHERE comment_id = #{commentId}")
    Long findBoardIdByCommentId(@Param("commentId") Long commentId);
    List<CommentsDTO> findAllByBoardIdAndBoardTypeSortTypeAndCursor(CommentsRepoDTO commentsRepoDTO);

    List<CommentsDTO> findAllByCommentIdAndBoardIdAndBoardType(@Param("commentsVO") CommentsDTO commentsDTO, @Param("largestCommentId") long largestCommentId);

    List<CommentsDTO> findAllByParentCommentId(@Param("commentId") long parentCommentId, @Param("sortType") String sortType, @Param("cursor") String cursor);

    @Delete("DELETE  FROM comments WHERE comment_id = #{commentId}")
    void deleteByCommentId(@Param("commentId") long commentId);
}


