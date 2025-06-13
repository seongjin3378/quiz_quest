package com.seong.portfolio.quiz_quest.comments.service;

import java.util.List;


/*
* ProblemCommentsVO에 인자값에 commentId가 없을 시
* - 부모댓글이 있는 답글을 저장하는 요청
* - problemCommentsRepository.findAllByCommentIdAndProblemId() 실행 안함
* 반면 commentId가 있다면
* - 일반 댓글 작성하는 요청이며 problemCommentsRepository.findAllByCommentIdAndProblemId() 함수를 실행함
* - findAllByCommentIdAndProblemId()는 최신순 정렬("DESC")에서 일반 댓글을 작성 시 요청 가능
*   - 클라이언트에서 이 함수에 들어가는 largestCommentId(최신 댓글 Id)를 전송하여 largestCommentId를
*   기준으로 댓글들을 클라이언트에 전송함(자신이 작성한 댓글포함)
*
*   - 예시로 largestCommentId는 오후 11시 21분인 최신 댓글 Id가 0이라 가정 시
*      - 내 댓글 작성시간은 오후 11시 23분(Id:3), 그전에 다른사람이 작성한 댓글은 11시 22분에 작성(Id:2),
*      - 댓글 작성 버튼을 누를 시 largestCommentId(Id)를 기준으로 Id2와 Id3(내 댓글)인 댓글을 불러와서 view 로 뿌려준다.
*
* */


public interface CommentService {
    List<Object> saveAndReturnComments(Object vo, String sortType);
    List<Object> findComments(long id, String sortType, String cursor);
    List<Object> findAllReplyComments(long parentCommentId, String sortType, String cursor);
}
