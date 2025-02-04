// script.js
let activeReplyListId = null; // 현재 활성화된 답글 목록 ID

function toggleReply(replyId) {
    const replySection = document.getElementById(replyId);
    replySection.style.display = replySection.style.display === 'none' ? 'block' : 'none';
}

async function toggleReplies(replyListId, commentId) {
    const replyListSection = document.getElementsByClassName("reply-list")[0];
    console.log(replyListId, commentId);
    await requestReplyComment(commentId, replyListId);
    const replyList = document.getElementById(replyListId);
    // 현재 활성화된 답글 목록이 있다면 닫기
    if (activeReplyListId && activeReplyListId !== replyListId) {
        document.getElementById(activeReplyListId).style.display = 'none';
    } else {
        replyListSection.style.display = replyListSection.style.display === 'none' ? 'block' : 'none';
    }

    // 답글 목록 토글
    if (replyList) { // 요소가 존재하는지 확인
        replyList.style.display = replyList.style.display === 'none' ? 'block' : 'none';
        activeReplyListId = replyList.style.display === 'block' ? replyListId : null;
        console.log("존재확인");
    }


}

async function requestReplyComment(parentCommentId, replyListId)
{
    try {
        // API URL 구성
        const replyListElement = document.getElementById(replyListId);
        let cursor;
        if (replyListElement) { // 댓글 엘리먼트가 있으면 커서 벨류 값 가져오기
            cursor = replyListElement.querySelector('.reply-cursor').value;
        }else{
            cursor="0"; // 없을 경우 커서 기본값으로 요청
        }

        const url = '/api/v1/problems/'+parentCommentId+'/'+cursor+'/ASC'+'/reply-comments';

        // GET 요청
        const response = await axios.get(url, {
            headers: {
                [csrfHeader]: csrfToken // CSRF 토큰을 헤더에 추가
            }
        });

        // 응답 데이터 처리
        const comments = response.data;
        console.log('댓글 목록:', comments);
        if (comments.length > 0) {
            createReplyComments(comments, parentCommentId, replyListId);
            cursor.value = comments[comments.length - 1].cursor;
        }
        // 필요한 추가 작업 수행

    } catch (error) {
        console.error('댓글 로드 실패:', error);
        // 오류 처리 로직
    }


}

function createReplyComments(comments, parentCommentId, replyListId)
{
    const replyHTML = [];
    comments.forEach((item2, index2) => {

         if (index2 === 0) {

             replyHTML.push(`<div><div class="reply" id="${replyListId}" style="display:none;">`);
         }
         replyHTML.push(`
              <div class="comment">
                 <div class="comment-header">
                     <span class="comment-author">작성자: ${item2.author}</span>
                     <span class="comment-date">작성일: ${item2.createdAt}</span>
                     <span class="comment-level">LEVEL ${item2.level}</span>
                 </div>
                 <p class="comment-body">${item2.commentContent}</p>
                 <div class="d-flex" style="gap: 5px; margin-top: 10px;">
                     <button type="button" class="btn btn-link reply-btn" onclick="toggleReply('reply-${replyListId}-${index2}')">답글 달기</button>
                     <button type="button" class="btn btn-link report-btn">신고</button>
                 </div>
                 <div class="reply-section" id="reply-${replyListId}-${index2}" style="display:none;">
                     <textarea class="form-control mt-2" placeholder="답글을 입력하세요..."></textarea>
                     <button type="button" class="btn btn-primary mt-2" onclick="requestWriteReply('${parentCommentId}', '${item2.author}', this)">답글 작성</button>
                 </div>
             </div>
     `);
     });

            replyHTML.push("<input type=\"hidden\" class=\"reply-cursor\" th:value=\"0\"/></div></div>");
            const newReplyHTML = replyHTML.join('');
            const lastReply = document.querySelector('.reply-list');
            lastReply.insertAdjacentHTML('beforeend', newReplyHTML);

}

function requestWriteComment(buttonElement)
{
        const url = '/api/v1/problems/comments';
        const commentValue = buttonElement.previousElementSibling.value;
        const comment = {
            commentContent: commentValue,
            problemId: problemIndexGlobal // 댓글이 달릴 문제 ID
        };
        axios.post(url, comment, {
            headers: {
                [csrfHeader]: csrfToken // CSRF 토큰을 헤더에 추가
            }
        })
            .then(response => {
                console.log('댓글이 추가되었습니다:', response.data);

            })
            .catch(error => {
                console.error('댓글 추가 중 오류 발생:', error);
            });

}

function requestWriteReply(commentId, receiver, buttonElement)
{
    const url = '/api/v1/problems/comments';
    const commentValue = buttonElement.previousElementSibling.value;
    let comment;
    if(receiver === 'none') //리시버가 없을 경우 2차 답글에 대한 요청이 아님
    {
        comment = {
            commentContent: commentValue,
            parentCommentId: commentId,
            problemId: problemIndexGlobal
        };
    }else{ // 2차 답글에 대한 요청일 경우 답글 작성자 이름을 넣음
        comment = {
            commentContent: '@'+receiver+' '+commentValue,
            parentCommentId: commentId,
            problemId: problemIndexGlobal
        };
        console.log('2차답글');
    }

    axios.post(url, comment, {
        headers: {
            [csrfHeader]: csrfToken // CSRF 토큰을 헤더에 추가
        }
    })
        .then(response => {
            console.log('댓글이 추가되었습니다:', response.data);

        })
        .catch(error => {
            console.error('댓글 추가 중 오류 발생:', error);
        });
}

document.addEventListener('DOMContentLoaded', function () {
    const commentSection = document.getElementsByClassName('comment-section')[0];
    commentSection.addEventListener('scroll', function () {
        // 스크롤이 마지막에 도달했는지 확인
        console.log(commentSection.scrollTop + commentSection.clientHeight);
        console.log(commentSection.scrollHeight);
        if (commentSection.scrollTop + commentSection.clientHeight + 2 >= commentSection.scrollHeight) {
            console.log('댓글 섹션의 마지막에 도달했습니다.');
            // 추가 댓글 로드 함수 호출
            const cursor = document.getElementById('cursor');

            requestComments(cursor.value).then(r => {
            })
        }
    });
});

async function requestComments(cursorId) {
    try {
        // API URL 구성
        const url = '/api/v1/problems/' + problemIndexGlobal + '/' + cursorId + '/DESC/comments';

        // GET 요청
        const response = await axios.get(url, {
            headers: {
                [csrfHeader]: csrfToken // CSRF 토큰을 헤더에 추가
            }
        });

        // 응답 데이터 처리
        const comments = response.data;
        console.log('댓글 목록:', comments);
        if (comments.length > 0) {
            createComments(comments);
        }
        // 필요한 추가 작업 수행

    } catch (error) {
        console.error('댓글 로드 실패:', error);
        // 오류 처리 로직
    }

}

let commentIndex = 10; // 타임리프로 생성한거 이후로 인덱스를 생성해야하기 때문에 10부터 시작


function createComments(comments) {
    comments.forEach((item, index) => {
        // 댓글을 추가할 HTML 생성
        const commentHTML = `
                <div class="comment">
                    <div class="comment-header">
                        <span class="comment-author">작성자: ${item.author}</span>
                        <span class="comment-date">작성일: ${item.createdAt}</span>
                        <span class="comment-level">LEVEL ${item.level}</span>
                    </div>
                    <p class="comment-body">${item.commentContent}</p>
                    <div class="d-flex" style="gap: 5px; margin-top: 10px;">
                        <button type="button" class="btn btn-link reply-btn" onclick="toggleReply('reply${commentIndex}')">답글 달기</button>
                        <button type="button" class="btn btn-link more-btn" onclick="toggleReplies('replies${commentIndex}', ${item.commentId})">답글 보기(${item.replyCount})</button>
                        <button type="button" class="btn btn-link report-btn">신고</button>
                    </div>
                    <div class="reply-section" id="reply${commentIndex}" style="display:none;">
                        <textarea class="form-control mt-2" placeholder="답글을 입력하세요..."></textarea>
                        <button type="button" class="btn btn-primary mt-2" onclick="requestWriteReply('${item.commentId}', 'none', this)">답글 작성</button>
                    </div>
                </div>
            `;
        commentIndex += 1;
        const replyList = item.replyCommentList;

        const replyHTML = [];
       /* replyList.forEach((item2, index2) => {

            if (index2 === 0) {

                replyHTML.push(`<div><div class="reply" id="replies${commentIndex}" style="display:none;">`);
            }
            replyHTML.push(`
                 <div class="comment">
                    <div class="comment-header">
                        <span class="comment-author">작성자: ${item2.replyAuthor}</span>
                        <span class="comment-date">작성일: ${item2.replyCreatedAt}</span>
                        <span class="comment-level">LEVEL ${item2.replyLevel}</span>
                    </div>
                    <p class="comment-body">${item2.replyCommentContent}</p>
                    <div class="d-flex" style="gap: 5px; margin-top: 10px;">
                        <button type="button" class="btn btn-link reply-btn" onclick="toggleReply('replyReply${commentIndex}-${index2}')">답글 달기</button>
                        <button type="button" class="btn btn-link report-btn">신고</button>
                    </div>
                    <div class="reply-section" id="replyReply${commentIndex}-${index2}" style="display:none;">
                        <textarea class="form-control mt-2" placeholder="답글을 입력하세요..."></textarea>
                        <button type="button" class="btn btn-primary mt-2" onclick="requestWriteReply('${item.commentId}', '${item2.replyAuthor}', this)">답글 작성</button>
                    </div>
                </div>
        `);
        });*/
/*        commentIndex += 1;
        replyHTML.push("</div></div>");
        const newReplyHTML = replyHTML.join('');
        const lastReply = document.querySelector('.reply-list');
        lastReply.insertAdjacentHTML('beforeend', newReplyHTML);*/

        // 마지막 comment 요소 아래에 추가

        const cursor = document.getElementById("cursor");
        cursor.value = comments[comments.length - 1].cursor;
        const lastComment = document.querySelector('.comment:last-child');

        lastComment.insertAdjacentHTML('afterend', commentHTML);
    });
}
