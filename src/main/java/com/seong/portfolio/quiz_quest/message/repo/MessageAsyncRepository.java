package com.seong.portfolio.quiz_quest.message.repo;

import com.seong.portfolio.quiz_quest.message.vo.MessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;



public interface MessageAsyncRepository {
    CompletableFuture<Integer> save(MessageVO messageVO);

}
