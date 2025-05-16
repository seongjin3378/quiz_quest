package com.seong.portfolio.quiz_quest.notification.repo.unused;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;


@Repository
@Slf4j
@RequiredArgsConstructor

/*사용 안함*/
public class MessageAsyncRepositoryImpl implements MessageAsyncRepository {

    /*private final SqlSessionTemplate template;
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
    }*/
}
