package com.seong.portfolio.quiz_quest.comments.service;

import com.seong.portfolio.quiz_quest.comments.dto.CommentsRepoDTO;
import com.seong.portfolio.quiz_quest.comments.repo.CommentsRepository;
import com.seong.portfolio.quiz_quest.comments.dto.CommentsDTO;
import com.seong.portfolio.quiz_quest.user.service.session.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommentServiceImpl implements CommentService {
    private final CommentsRepository commentsRepository;
    private final SessionService sessionService;

    private int getCommentIdOrDefault(CommentsDTO commentsDTO) {
        return Math.toIntExact(Optional.ofNullable(commentsDTO)
                .map(CommentsDTO::getCommentId)
                .orElse((long) -1));
    }

    @Override
    @Transactional
    public List<Object> saveAndReturnComments(Object vo, String sortType) {
        CommentsDTO commentsDTO = (CommentsDTO) vo;
        commentsDTO.setAuthor(sessionService.getSessionId());
        long largestCommentId = getCommentIdOrDefault(commentsDTO); // largestCommentId가 null일 경우 -1 반환
        log.info("getCommentId: {}", commentsDTO.getCommentId());
        commentsRepository.save(commentsDTO); // save 하는 과정에서 생성된 commentId를 problemCommentsVO에 자동으로 할당됨
        List<CommentsDTO> result;
        boolean isNotReplyCommentReq = commentsDTO.getParentCommentId() == null;

        if(sortType.equals("DESC")||isNotReplyCommentReq) { // 댓글이 아닌 답글 작성 요청이거나 최신순 정렬일 경우
            result = commentsRepository.findAllByCommentIdAndBoardIdAndBoardType(commentsDTO, largestCommentId);
            return Collections.singletonList(result);
        }

        return List.of();
    }

    @Override
    public List<Object> findComments(long id, String sortType, String cursor, String boardType) {
        return Collections.singletonList(commentsRepository.findAllByBoardIdAndBoardTypeSortTypeAndCursor(getCommentRepoDTO(id, sortType, cursor, boardType)));
    }
    private CommentsRepoDTO getCommentRepoDTO(long id, String sortType, String cursor, String boardType)
    {
        return CommentsRepoDTO.builder()
                .boardId(id)
                .sortType(sortType)
                .cursor(cursor)
                .boardType(boardType)
                .build();
    }

    @Override
    public List<Object> findAllReplyComments(long parentCommentId, String sortType, String cursor) {

        if(cursor.equals("0")) {
            return Collections.singletonList(commentsRepository
                    .findAllByParentCommentId(parentCommentId, sortType, "0"));

        }else{
            return Collections.singletonList(commentsRepository
                    .findAllByParentCommentId(parentCommentId, sortType, cursor));
        }
    }
}
