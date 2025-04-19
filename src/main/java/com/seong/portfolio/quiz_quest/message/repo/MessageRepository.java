package com.seong.portfolio.quiz_quest.message.repo;


import com.seong.portfolio.quiz_quest.message.vo.MessageVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MessageRepository {
    List<MessageVO> findAllBySenderId(long senderId);
}
