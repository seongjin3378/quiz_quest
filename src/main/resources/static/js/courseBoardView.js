const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

// 초기 상태 변수 선언
//  state =  1 → 좋아요 상태
//  state = -1 → 싫어요 상태
//  state =  0 → 아무것도 누른 상태가 아님(기본)
let currentState = 0;

let likes    = 0;
let dislikes = 0;

// 버튼, 카운트 엘리먼트
const likeBtn      = document.getElementById('likeBtn');
const dislikeBtn   = document.getElementById('dislikeBtn');
const likeCount    = document.getElementById('likeCount');
const dislikeCount = document.getElementById('dislikeCount');

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
        likes    = responseData.likes;
        dislikes = responseData.dislikes;
        currentState = responseData.currentState; // 서버에서 현재 상태(1/-1/0) 리턴받는 경우
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
    if (currentState === 1) {
        likeBtn.classList.add('active');    // CSS에서 .active { color: blue; } 같은 스타일 지정
        dislikeBtn.classList.remove('active');
    } else if (currentState === -1) {
        dislikeBtn.classList.add('active');
        likeBtn.classList.remove('active');
    } else {
        likeBtn.classList.remove('active');
        dislikeBtn.classList.remove('active');
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