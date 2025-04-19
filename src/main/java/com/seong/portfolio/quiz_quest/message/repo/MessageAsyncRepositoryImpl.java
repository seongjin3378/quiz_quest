package com.seong.portfolio.quiz_quest.message.repo;

import com.seong.portfolio.quiz_quest.message.vo.MessageVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.SqlSessionTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.CompletableFuture;


@Repository
@Slf4j
@RequiredArgsConstructor
public class MessageAsyncRepositoryImpl implements MessageAsyncRepository {

    private final SqlSessionTemplate template;
    private final static String NAME_SPACE = "com.seong.portfolio.quiz_quest.message.repo.MessageRepository";
    @Override
    public CompletableFuture<Integer> save(MessageVO messageVO) {
        long startTime = System.currentTimeMillis();
        return CompletableFuture.supplyAsync(() -> {
            int messageId = template.insert(NAME_SPACE+".save", messageVO);
            long endTime = System.currentTimeMillis();
            log.info("getMessageId: {} ms", endTime - startTime);

            return messageId;
        });
    }
}
