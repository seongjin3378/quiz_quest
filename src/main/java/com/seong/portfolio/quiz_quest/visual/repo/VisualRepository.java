package com.seong.portfolio.quiz_quest.visual.repo;


import com.seong.portfolio.quiz_quest.visual.dto.VisualDTO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface VisualRepository {
    void save(VisualDTO visualDTO);
    @Select("SELECT visual_src FROM visual WHERE visual_id = #{visualId};")
    String findVisualSrcByProblemVisualId(@Param("visualId") long visualId);
    List<VisualDTO> findAllVisualByBoardIdAndBoardType(@Param("boardId") long boardId, @Param("boardType") String boardType);
}
