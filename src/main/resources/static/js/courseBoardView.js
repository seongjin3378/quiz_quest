const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

// 초기 상태 변수 선언
//  state =  1 → 좋아요 상태
//  state = -1 → 싫어요 상태
//  state =  0 → 아무것도 누른 상태가 아님(기본)
let currentState =  parseInt(document.getElementById("state-text").textContent);


let likes    = 0;
let dislikes = 0;
let cursor = 0;

// 버튼, 카운트 엘리먼트
const likeBtn      = document.getElementById('likeBtn');
const dislikeBtn   = document.getElementById('dislikeBtn');
const likeCount    = document.getElementById('likeCount');
const dislikeCount = document.getElementById('dislikeCount');


updateLikeDislikeState(currentState, likeCount, dislikeCount);

// URL에서 courseId(=마지막 숫자) 가져오는 함수
function getCourseId() {
    const path = window.location.pathname; // 예: "/c/n/51"
    return path.split('/').pop();
}



// 좋아요/싫어요 요청을 보낸 뒤, 버튼 색상·카운트 등을 업데이트하는 함수
function updateUI(newState, responseData) {
    // responseData에 서버에서 보내준 최종 카운트 정보가 있다고 가정하면
    // 예) { likes: 10, dislikes: 3, state: 1 }
    // 서버 로직에 맞춰 필드명을 조정하세요.
    if (responseData) {
        likes    = responseData.totalLikes;
        dislikes = responseData.totalDisLikes;
        currentState = responseData.currentState; // 서버에서 현재 상태(1/-1/0) 리턴받는 경우

        console.log(responseData);
    } else {
        // 간단히 클라이언트에서 상태만 토글하고, 숫자를 +1/-1/-1/+1 하려면 아래처럼 할 수 있습니다.
        // 다만, 실제 서비스라면 서버에서 최종 counts를 내려주는 게 정확합니다.
        if (newState === 1) {
            if (currentState === 0) {
                likes++;
            } else if (currentState === -1) {
                dislikes--;
                likes++;
            }
        } else if (newState === -1) {
            if (currentState === 0) {
                dislikes++;
            } else if (currentState === 1) {
                likes--;
                dislikes++;
            }
        } else if (newState === 0) {
            // 취소 요청: 이전 상태가 좋아요면 likes--, 싫어요면 dislikes--
            if (currentState === 1) {
                likes--;
            } else if (currentState === -1) {
                dislikes--;
            }
        }
        currentState = newState;
    }

    // 화면에 반영

    likeCount.textContent    = likes;
    dislikeCount.textContent = dislikes;

    // 버튼 색상 토글 (예: 눌려 있을 땐 파란색, 아닐 땐 기본)
    updateLikeDislikeState(currentState, likeCount, dislikeCount)
}

function updateLikeDislikeState(currentState, likeCountElem, dislikeCountElem) {
    if (currentState === 1) {
        likeCountElem.classList.add('active');
        dislikeCountElem.classList.remove('active');
    } else if (currentState === -1) {
        dislikeCountElem.classList.add('active');
        likeCountElem.classList.remove('active');
    } else { // currentState === 0 혹은 그 외
        likeCountElem.classList.remove('active');
        dislikeCountElem.classList.remove('active');
    }
}

// 좋아요 버튼 클릭 핸들러
likeBtn.addEventListener('click', () => {
    const courseId = getCourseId();

    // 현재가 '좋아요 상태(1)'이면 → 취소(0) 요청, 아니면 좋아요(1) 요청
    const newState = (currentState === 1) ? 0 : 1;

    axios.post(`/api/v1/courses/like/${courseId}/${newState}`, {}, {
        headers: {
            [csrfHeader]: csrfToken
        }
    })
        .then(response => {
            // 서버가 { likes, dislikes, state } 형태로 리턴한다고 가정
            updateUI(newState, response.data);
        })
        .catch(error => {
            console.error('Error:', error);
            // 실패 시 로그만 찍고 UI 변경 안 함
        });
});

// 싫어요 버튼 클릭 핸들러
dislikeBtn.addEventListener('click', () => {
    const courseId = getCourseId();

    // 현재가 '싫어요 상태(-1)'이면 → 취소(0) 요청, 아니면 싫어요(-1) 요청
    const newState = (currentState === -1) ? 0 : -1;

    axios.post(`/api/v1/courses/like/${courseId}/${newState}`, {}, {
        headers: {
            [csrfHeader]: csrfToken
        }
    })
        .then(response => {
            updateUI(newState, response.data);
        })
        .catch(error => {
            console.error('Error:', error);
        });
});



// --- 댓글 작성 부분 (기존 코드와 동일) ---
const commentForm   = document.getElementById('commentForm');
const commentInput  = document.getElementById('commentInput');
const commentsList  = document.getElementById('commentsList');

commentForm.addEventListener('submit', e => {
    e.preventDefault();
    const text = commentInput.value.trim();
    if (!text) return;

    const commentEl = document.createElement('div');
    commentEl.className = 'comment';
    commentEl.innerHTML = `
    <div class="comment-header">익명 | ${new Date().toLocaleString()}</div>
    <div class="comment-body">${text}</div>
  `;
    commentsList.prepend(commentEl);
    commentInput.value = '';
});




document.addEventListener('DOMContentLoaded', async () => {
    const commentsList = document.getElementById('commentsList');
    const commentForm = document.getElementById('commentForm');
    const commentInput = document.getElementById('commentInput');
    let isLoading = false;

    // 1) 댓글 불러오기
    function renderComment(comment) {
        const div = document.createElement('div');
        div.className = 'comment';
        div.dataset.id = comment.id;
        div.innerHTML = `
    <p class="text">${escapeHtml(comment.commentContent)}</p>
    <small>${comment.author} • ${new Date(comment.createdAt).toLocaleString()}</small>
    <div class="actions">
      <button class="editBtn">수정</button>
      <button class="deleteBtn">삭제</button>
    </div>
  `;
        div.querySelector('.editBtn')
            .addEventListener('click', () => startEdit(div, comment));
        div.querySelector('.deleteBtn')
            .addEventListener('click', () => deleteComment(div.dataset.id));
        return div;
    }

// --- 페이지네이션용 로드 (append) ---
    async function loadComments(sortType, cursor) {
        try {
            // 1) POST 요청 보내기
            const url = `/api/v1/courses/${courseId}/${cursor}/${sortType}/comments`;
            console.log(url);

            axios.post(url, {}, {
                headers: {
                    [csrfHeader]: csrfToken
                }
            })
                .then(response => {
                    const comments = response.data[0];
                    console.log(comments);
                    // 4) 각 댓글 렌더링 및 DOM 에 추가
                    comments.forEach(c => {
                        const el = renderComment(c);
                        commentsList.appendChild(el);
                    });

                    // 5) 다음 cursor 처리
                    //    예: 마지막 댓글 ID를 반환해서, 더 불러올 때 사용
                    return comments.length > 0 ? comments[comments.length - 1].id : null;
                })
                .catch(error => {
                    console.error('Error:', error);
                    // 실패 시 로그만 찍고 UI 변경 안 함
                });





        }

        catch (error) {
            console.error('댓글 로드 중 오류 발생:', error);
            // 에러 UI 처리 (토스트, 얼러트 등)
        }
    }


// --- 댓글 등록 핸들러: 이제 renderComment 사용 ---
    commentForm.addEventListener('submit', async e => {
        e.preventDefault();
        const text = commentInput.value.trim();
        if (!text) return;

        try {
            const res = await axios.post(`/courses/${courseId}/comments`, {text}, {
                headers: {[csrfHeader]: csrfToken}
            });
            // 새로 생성된 댓글 element를 맨 위로 prepend
            const newEl = renderComment(res.data);
            commentsList.prepend(newEl);
            commentInput.value = '';
        } catch (err) {
            console.error('댓글 등록 실패', err);
        }
    });

    // 4) 댓글 삭제
    async function deleteComment(commentId) {
        if (!confirm('정말 삭제하시겠습니까?')) return;
        try {
            await axios.delete(`/comments/${commentId}`);
            const el = commentsList.querySelector(`.comment[data-id="${commentId}"]`);
            if (el) el.remove();
        } catch (err) {
            console.error('댓글 삭제 실패', err);
        }
    }

    // 5) 댓글 수정 시작
    function startEdit(container, comment) {
        const p = container.querySelector('.text');
        const original = p.textContent;
        // textarea + 확인/취소 버튼으로 대체
        container.innerHTML = `
      <textarea class="editInput">${escapeHtml(original)}</textarea>
      <div>
        <button class="saveBtn">저장</button>
        <button class="cancelBtn">취소</button>
      </div>
    `;
        container.querySelector('.saveBtn').addEventListener('click', () => saveEdit(container, comment.id));
        container.querySelector('.cancelBtn').addEventListener('click', () => {
            container.innerHTML = '';
            renderComment(comment);
        });
    }

    // 6) 댓글 수정 저장
    async function saveEdit(container, commentId) {
        const newText = container.querySelector('.editInput').value.trim();
        if (!newText) return alert('댓글을 입력하세요');
        try {
            const res = await axios.put(`/comments/${commentId}`, {text: newText});
            // 수정된 댓글 다시 렌더링
            container.innerHTML = '';
            renderComment(res.data);
        } catch (err) {
            console.error('댓글 수정 실패', err);
        }
    }

    // 7) 스크롤 페이징
    window.addEventListener('scroll', () => {
        // 아래로 300px 정도 남았을 때 추가 로딩
        if (window.innerHeight + window.scrollY >= document.body.offsetHeight - 300) {
            cursor = loadComments("DESC", cursor);
        }
    });

    // escapeHtml: XSS 방지
    function escapeHtml(str) {
        console.log(str)
        return str.replace(/[&<>"']/g, tag => ({
            '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;'
        }[tag]));
    }

    // 초기 댓글 로드
    cursor = await loadComments("DESC", cursor);
});