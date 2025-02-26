// script.js
let activeReplyListId = null; // 현재 활성화된 답글 목록 ID
let currentCommentId = null;
let currentRepliesId = null;
let currentSortType = 'DESC';

function toggleReply(replyId) {
    const replySection = document.getElementById(replyId);
    replySection.style.display = replySection.style.display === 'none' ? 'block' : 'none';
}


let replyListSection = document.querySelector('.reply-list');

replyListSection.addEventListener('scroll', function () {
    replyListSection = document.querySelector('.reply-list');

    // 스크롤이 마지막에 도달했는지 확인
    if (replyListSection.scrollTop + replyListSection.clientHeight + 2 >= replyListSection.scrollHeight) {
        if (currentCommentId) {
            console.log('댓글 섹션의 마지막에 도달했습니다.');

            requestReplyComment(currentCommentId, currentRepliesId).then(r => {

            });
            console.log(currentCommentId, currentRepliesId);
        }
    }
});


async function toggleReplies(replyListId, commentId) {
    const replyListSection = document.getElementsByClassName("reply-list")[0];
    console.log(replyListId, commentId);
    replyListSection.scrollTop = 0; // 스크롤 위치 초기화
    await requestReplyComment(commentId, replyListId);
    const replyList = document.getElementById(replyListId);
    // 현재 활성화된 답글 목록이 있다면 닫기
    const isComment = replyList.querySelector(".comment").length;
    if (activeReplyListId && activeReplyListId !== replyListId || isComment === 0) {
        document.getElementById(activeReplyListId).style.display = 'none';
    } else {
        replyListSection.style.display = replyListSection.style.display === 'none' ? 'block' : 'none';

    }

    // 답글 목록 토글
    if (replyList) { // 요소가 존재하는지 확인
        replyList.style.display = replyList.style.display === 'none' ? 'block' : 'none';
        activeReplyListId = replyList.style.display === 'block' ? replyListId : null;
        console.log("존재확인");
        if (replyList.style.display === 'block') {
            currentCommentId = commentId;
            currentRepliesId = replyListId;

        }
    }


}

async function requestReplyComment(parentCommentId, replyListId) {
    try {
        // API URL 구성
        let replyListElement = document.getElementById(replyListId);
        let cursor;
        if (replyListElement) { // 댓글 엘리먼트가 있으면 커서 벨류 값 가져오기
            const cursorElement = replyListElement.querySelector('.reply-cursor');
            cursor = cursorElement.value;
        } else {
            cursor = "0"; // 없을 경우 커서 기본값으로 요청
        }

        const url = '/api/v1/problems/' + parentCommentId + '/' + cursor + '/ASC' + '/reply-comments';

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
            await createReplyComments(comments, parentCommentId, replyListId);
        }
        replyListElement = document.getElementById(replyListId);
        if (replyListElement && comments.length > 0) {
            const cursorElement = replyListElement.querySelector('.reply-cursor');
            cursorElement.value = comments[comments.length - 1].cursor;

        }

    } catch (error) {
        console.error('댓글 로드 실패:', error);
        // 오류 처리 로직
    }


}


let replyMap = new Map();

async function createReplyComments(comments, parentCommentId, replyListId) {
    const replyHTML = [];
    const replyListElement = document.getElementById(replyListId);
    comments.forEach((item2, index2) => {

        if (index2 === 0 && !replyListElement) {

            replyHTML.push(`<div><div class="reply" id="${replyListId}" style="display:none;">`);
        }

        replyMap.set(replyListId, isNaN(replyMap.get(replyListId)) ? 0 : replyMap.get(replyListId) + 1);
        replyHTML.push(`
              <div class="comment">
                 <div class="comment-header">
                     <span class="comment-author">작성자: ${item2.author}</span>
                     <span class="comment-date">작성일: ${item2.createdAt}</span>
                     <span class="comment-level">LEVEL ${item2.level}</span>
                 </div>
                 <p class="comment-body">${item2.commentContent}</p>
                 <div class="d-flex" style="gap: 5px; margin-top: 10px;">
                     <button type="button" class="btn btn-link reply-btn" onclick="toggleReply('reply-${replyListId}-${replyMap.get(replyListId)}')">답글 달기</button>
                     <button type="button" class="btn btn-link report-btn">신고</button>
                 </div>
                 <div class="reply-section" id="reply-${replyListId}-${replyMap.get(replyListId)}" style="display:none;">
                     <textarea class="form-control mt-2" placeholder="답글을 입력하세요..."></textarea>
                     <button type="button" class="btn btn-primary mt-2" data-comment-id= ${item2.commentId} onclick="requestWriteReply('${parentCommentId}', '${item2.author}', this)">답글 작성</button>
                 </div>
             </div>
     `);

    });

    if (!replyListElement) {
        replyHTML.push("<input type=\"hidden\" class=\"reply-cursor\" value=\"0\"/></div></div>");
    }

    const newReplyHTML = replyHTML.join('');

    let lastReply;
    if (!replyListElement) {
        lastReply = document.querySelector('.reply-list');
    } else {
        lastReply = replyListElement;
    }
    lastReply.insertAdjacentHTML('beforeend', newReplyHTML);

    if (!replyListSection.querySelector('br')) {
        lastReply.insertAdjacentHTML('beforeend', "<br><br><br><br><br><br><br><br><br><br><br><br>");
    } else {
        const brElements = replyListSection.querySelectorAll('br');
        brElements.forEach(br => {
            replyListSection.appendChild(br);
        });
    }

}

function requestWriteComment(buttonElement) {
    const url = '/api/v1/problems/'+currentSortType+'/comments';
    const commentValue = buttonElement.previousElementSibling.value;
    const commentSection = document.querySelector('.comment-section');
    const secondComment = commentSection.children[1];

    let largestCommentId = 0;
    if (secondComment) {
        largestCommentId = secondComment.querySelector('[data-comment-id]').dataset['commentId'];
    }

    console.log(largestCommentId);

    let comment;
    if(currentSortType === 'ASC')
    {
    comment = {
        commentContent: commentValue,
        problemId: problemIndexGlobal, // 댓글이 달릴 문제 ID
        commentId: largestCommentId
    };
    }
    else{ //DESC 일 경우
        comment = {
            commentContent: commentValue,
            problemId: problemIndexGlobal, // 댓글이 달릴 문제 ID
            commentId: largestCommentId
        };
    }
    axios.post(url, comment, {
        headers: {
            [csrfHeader]: csrfToken // CSRF 토큰을 헤더에 추가
        }
    })
        .then(response => {
            console.log('댓글이 추가되었습니다:', response.data);
            if(currentSortType === 'ASC')
            {

                const sortLatestButton = document.getElementById('sortLatest');
                if (sortLatestButton) {
                    sortLatestButton.click();
                    return;
                }
            }
            createComments(response.data, secondComment);


        })
        .catch(error => {
            console.error('댓글 추가 중 오류 발생:', error);
        });


}

function removeParentIfCommentIdExists() {
    const comments = document.querySelectorAll('.comment');
    const commentSection = document.querySelector('.comment-section'); // 댓글 섹션 선택
    const commentsAll = commentSection.querySelectorAll('[data-comment-id]'); // data-comment-id가 있는 모든 댓글 요소 선택

    // comments 배열에 있는 각 댓글 요소의 data-comment-id를 배열로 변환
    const commentIds = Array.from(comments).map(comment => {
        return comment.getAttribute('data-comment-id'); // 각 댓글의 data-comment-id 가져오기
    });

    // commentsAll에서 중복된 data-comment-id가 있는 경우 부모 삭제
    commentsAll.forEach(comment => {
        const commentId = comment.getAttribute('data-comment-id'); // data-comment-id 가져오기

        if (commentIds.includes(commentId)) {
            // data-comment-id가 comments에 있는 경우 부모 삭제
            comment.parentElement.remove();
        }
    });
}



function requestWriteReply(commentId, receiver, buttonElement) {
    const url = '/api/v1/problems/'+currentSortType+'/comments';
    const commentValue = buttonElement.previousElementSibling.value;

    let comment;
    if (receiver === 'none') //리시버가 없을 경우 2차 답글에 대한 요청이 아님
    {
        comment = {
            commentContent: commentValue,
            parentCommentId: commentId,
            problemId: problemIndexGlobal
        };
    } else { // 2차 답글에 대한 요청일 경우 답글 작성자 이름을 넣음
        comment = {
            commentContent: '@' + receiver + ' ' + commentValue,
            parentCommentId: commentId,
            problemId: problemIndexGlobal,
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

            const replySections = document.querySelectorAll('[class^="reply-section"]');
            const blockElements = Array.from(replySections).filter(element => {
                return getComputedStyle(element).display === 'block';
            });

            blockElements.forEach(element => {
                element.style.display = 'none';
            });
        })
        .catch(error => {
            console.error('댓글 추가 중 오류 발생:', error);
        });

}

document.addEventListener('DOMContentLoaded', async function () {
    const commentSection = document.getElementsByClassName('comment-section')[0];
    commentSection.addEventListener('scroll', function () {
        // 스크롤이 마지막에 도달했는지 확인

        if (commentSection.scrollTop + commentSection.clientHeight + 2 >= commentSection.scrollHeight) {
            console.log('댓글 섹션의 마지막에 도달했습니다.');
            // 추가 댓글 로드 함수 호출
            const cursor = document.getElementById('cursor');

            requestComments(cursor.value, currentSortType).then(r => {
            })


        }
    });


});

async function requestComments(cursorId, sortType) {
    try {
        // API URL 구성
        const url = '/api/v1/problems/' + problemIndexGlobal + '/' + cursorId + '/'+sortType+'/comments';

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


function createComments(comments, insertElement) {
    comments.forEach((item, index) => {
        // 댓글을 추가할 HTML 생성
        console.log(item);
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
                        <button type="button" class="btn btn-primary mt-2" data-comment-id= ${item.commentId} onclick="requestWriteReply('${item.commentId}', 'none', this)">답글 작성</button>
                    </div>
                </div>
            `;
        commentIndex += 1;

        const cursor = document.getElementById("cursor");


        const lastComment = document.querySelector('.comment:last-child');
        if(!lastComment)
        {

        const commentSection = document.querySelector('.comment-section');
        addCommentSection(commentSection, commentHTML); // 함수 호출
        }
        else if (!insertElement) {
            lastComment.insertAdjacentHTML('afterend', commentHTML);
            cursor.value = comments[comments.length - 1].cursor;
        } else {
            insertElement.insertAdjacentHTML('beforebegin', commentHTML);
            console.log("에프터 비긴!");
        }
    });
}


/*정렬 버튼 누를 시 */
function addCommentSection(commentSection, commentHTML) {
    const firstCommentHTML = `
        <div class="comment">
            <textarea class="form-control mt-2" placeholder="답글을 입력하세요..."></textarea>
            <button type="button" class="btn btn-primary mt-2" onClick="requestWriteComment(this)">답글 작성</button>
        </div>
    `;

    // 기존 내용을 유지하면서 HTML 문자열 추가
    commentSection.innerHTML += firstCommentHTML+ commentHTML;
}


function clearModalContent() {
    // modal-body 선택

    // modal-body에서 input 태그의 value 값을 0으로 변경


    // .commentSection 안의 자식 요소 모두 삭제
    const commentSection = document.querySelector('.comment-section');
    if (commentSection) {
        while (commentSection.firstChild) {
            commentSection.firstChild.remove();
        }
    }

    // .reply-list 안의 자식 요소 모두 삭제
    const replyList = document.querySelector('.reply-list');
    if (replyList) {
        while (replyList.firstChild) {
            replyList.firstChild.remove();
        }
    }
    const inputElements = document.querySelectorAll('input');

    inputElements.forEach(input => input.value = '0');
}
document.getElementById('sortLatest').addEventListener('click', function() {
    document.getElementById('sortButton').textContent = '최신순';
    // 최신순 정렬 로직 추가
    clearModalContent()
    currentSortType = 'DESC';
    replyListSection.style.display = 'none';
    resetId()
    requestComments("0", currentSortType).then(r => {
    });

});

function resetId()
{
    activeReplyListId = null; // 현재 활성화된 답글 목록 ID
    currentCommentId = null;
    currentRepliesId = null;
    replyMap = new Map();
}
document.getElementById('sortOldest').addEventListener('click', function() {
    document.getElementById('sortButton').textContent = '오래된 순';
    clearModalContent()
    currentSortType = 'ASC';
    replyListSection.style.display = 'none';
    resetId()

    requestComments("0", currentSortType).then(r => {
    });
    // 오래된 순 정렬 로직 추가
});
