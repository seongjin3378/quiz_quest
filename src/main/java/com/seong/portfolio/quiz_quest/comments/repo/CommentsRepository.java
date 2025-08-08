package com.seong.portfolio.quiz_quest.comments.repo;

import com.seong.portfolio.quiz_quest.comments.dto.CommentsDTO;
import com.seong.portfolio.quiz_quest.comments.dto.CommentsRepoDTO;
import org.apache.ibatis.annotations.*;

import java.util.List;



@Mapper
public interface CommentsRepository {
    long save(CommentsDTO commentsDTO);

    @Select("SELECT board_id FROM comments WHERE comment_id = #{commentId}")
    Long findBoardIdByCommentId(@Param("commentId") Long commentId);
    List<CommentsDTO> findAllByBoardIdAndBoardTypeSortTypeAndCursor(CommentsRepoDTO commentsRepoDTO);

    List<CommentsDTO> findAllByCommentIdAndBoardIdAndBoardType(@Param("commentsVO") CommentsDTO commentsDTO, @Param("largestCommentId") long largestCommentId);

    List<CommentsDTO> findAllByParentCommentId(@Param("parentCommentId") long parentCommentId, @Param("sortType") String sortType, @Param("cursor") String cursor);

    @Delete("DELETE  FROM comments WHERE comment_id = #{commentId}")
    void deleteByCommentId(@Param("commentId") long commentId);


    // 업데이트 이후 조회시 이전 데이터가 조회 될 수 있어 캐시를 비워줘야함
    @Update("UPDATE comments SET comment_content = #{commentContent} WHERE comment_id = #{commentId}")
    @Options(flushCache = Options.FlushCachePolicy.TRUE
    )
    void updateByCommentId(CommentsDTO dto);

    @Options(useCache = false)
    CommentsDTO findOneByCommentIdAndBoardType(CommentsDTO dto);

    //
}


