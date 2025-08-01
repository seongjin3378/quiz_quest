package com.seong.portfolio.quiz_quest.comments.repo;

import com.seong.portfolio.quiz_quest.comments.dto.CommentsDTO;
import com.seong.portfolio.quiz_quest.comments.dto.CommentsRepoDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface CommentsRepository {
    long save(CommentsDTO commentsDTO);

    List<CommentsDTO> findAllByBoardIdAndBoardTypeSortTypeAndCursor(CommentsRepoDTO commentsRepoDTO);

    List<CommentsDTO> findAllByCommentIdAndBoardIdAndBoardType(@Param("commentsVO") CommentsDTO commentsDTO, @Param("largestCommentId") long largestCommentId);

    List<CommentsDTO> findAllByParentCommentId(@Param("commentId") long parentCommentId, @Param("sortType") String sortType, @Param("cursor") String cursor);
}


