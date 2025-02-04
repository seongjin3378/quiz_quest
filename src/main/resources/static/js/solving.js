const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    sidebar.style.right = sidebar.style.right === '0px' ? '-220px' : '0px';
}


function formatTime(seconds) {
    // 분과 초 계산
    const minutes = Math.floor(seconds / 60);
    const remainingSeconds = seconds % 60;

    // 2자리 형식으로 포맷 (예: 00:00)
    const formattedMinutes = String(minutes).padStart(2, '0');
    const formattedSeconds = String(remainingSeconds).padStart(2, '0');

    return `${formattedMinutes}:${formattedSeconds}`;
}

// 300초를 포맷


(function () {
    function increaseTimer() {

        console.log("1: ", csrfToken, "2:", csrfHeader)
        const timerInterval = setInterval(() => {
            let timer = document.getElementById('usage-time');
            let timerView = document.getElementById('usage-time-view');
            let increasedTimer = parseInt(timer.value, 10) + 1;
            timer.value = increasedTimer.toString();
            timerView.textContent = formatTime(Number(timer.value));

            if (increasedTimer % 60 === 0) {
                console.log("인크리즈 타이머 실행 : ");
                const url = "/api/v1/rankings/usage-timers";
                const UserUsageTimerVO = {
                    userUsageTimer: increasedTimer,
                };


                //for (let i = 0; i < 2; i++) { //TooManyRequest 테스트를 위해 작성
                axios.put(url, UserUsageTimerVO, {
                    headers: {
                        [csrfHeader]: csrfToken // CSRF 토큰을 헤더에 추가
                    }
                })

                    .then(response => {
                        if(response.data !== 1)
                        {
                            timer.value = response.data.toString();
                            timerView.textContent = formatTime(Number(timer.value));
                        }
                    }).catch((error) => {
                    if (error.response) {
                        // 요청이 이루어졌고, 서버가 상태 코드로 응답했을 경우
                        console.log("서버 응답 오류: " + error.response.status);
                        console.log("응답 데이터: " + error.response.data);
                    } else if (error.request) {
                        // 요청이 이루어졌으나 응답이 없었을 경우
                        console.log("응답이 없습니다: " + error.request);
                    } else {
                        // 오류를 발생시킨 요청 설정 중 오류
                        console.log("오류 메시지: " + error.message);
                    }
                });
            }
            //}
        }, 1000);
    }

    // increaseTimer 함수를 호출하여 타이머 시작
    increaseTimer();
})();

function nextProblem() {
    alert("다음 문제로 이동합니다.");
}

function submitComment() {
    alert("댓글이 작성되었습니다.");
}

function updateExperienceBar() {
    const experienceFill = document.getElementById('experienceFill');
    experienceFill.style.width = '70%'; // 원하는 경험치 비율로 설정
}
function showError(message) {
    const errorMessageDiv = document.getElementById('errorMessage');
    const errorText = document.getElementById('errorText');
    errorText.textContent = message; // 에러 메시지 설정
    errorMessageDiv.style.display = 'block'; // 표시
    errorMessageDiv.style.opacity = '1'; // 초기 불투명도 설정

    // 10초 후에 서서히 사라지기 시작
    setTimeout(() => {
        errorMessageDiv.style.opacity = '0'; // 투명하게 만들기
        // 1초 후에 숨김 처리
        setTimeout(() => {
            errorMessageDiv.style.display = 'none'; // 완전히 숨김
        }, 1000); // 1초 후
    }, 10000); // 10초 후
}


document.getElementById('closeButton').addEventListener('click', function() {
    const errorMessageDiv = document.getElementById('errorMessage');
    errorMessageDiv.style.display = 'none'; // 숨김
});


function checkAnswer()
{
    let inputText = document.getElementById("code-area").value;

    const blob = new Blob([inputText], {type: 'text/plain'});
    const formData = new FormData();
    formData.append('file', blob, "file.py");
    const language = document.getElementById("combo-box").value;
    console.log(language);
    const url = '/api/v1/problems/'+problemIndexGlobal+'/'+language+'/upload-and-validate';
    console.log(url);

    const loadingOverlay = document.getElementById('loadingOverlay');
    loadingOverlay.style.display = 'flex';

    // 프로그레스 바 애니메이션
    let progress = 0;
    const progressBar = document.getElementById('progressBar');

    const interval = setInterval(() => {
        if (progress < 90) { // 90%까지 증가
            progress += 5; // 5%씩 증가
            progressBar.style.width = progress + '%';
        }
    }, 500); // 100ms마다 증가


    axios.post(url, formData, {
        headers: {
            'Content-Type': 'multipart/form-data' // 멀티파트 전송을 위한 헤더
        }
    })
        .then(response => {
            // 성공적으로 응답을 받았을 때
            console.log('응답 데이터:', response.data);
            progress = 100;
            progressBar.style.width = progress + '%';
            clearInterval(interval); // 프로그레스 증가 중단
            setTimeout(() => {
                loadingOverlay.style.display = 'none';
            }, 500); // 0.5초 후에 숨김
            if(response.data === "Yes")
            {
                // 로딩 오버레이 숨기기
                const myModal = new bootstrap.Modal(document.getElementById('myModal'));
                progressBar.style.backgroundColor = '#25FF00';
                myModal.show();
                updateExperienceBar()
            }else{
                progressBar.style.backgroundColor = '#D32F2F';
            }
        })
        .catch(error => {
            // 에러가 발생했을 때
            const errorResponse = error.response.data;
            showError(errorResponse);

            progress = 100;
            progressBar.style.width = progress + '%';
            clearInterval(interval); // 프로그레스 증가 중단
            setTimeout(() => {
                loadingOverlay.style.display = 'none';
            }, 500); // 0.5초 후에 숨김
            progressBar.style.backgroundColor = '#D32F2F';
        });
}

function readCommentsBtn()
{
    const myModal = new bootstrap.Modal(document.getElementById('myModal'));
    myModal.hide();

    // 댓글 모달 열기
    const commentsModal = new bootstrap.Modal(document.getElementById('commentsModal'));
    commentsModal.show();
}

document.addEventListener("DOMContentLoaded", function() {
    let activeReplyListId = null; // 현재 활성화된 답글 목록 ID

    function toggleReply(replyId) {
        const replySection = document.getElementById(replyId);
        replySection.style.display = replySection.style.display === 'none' ? 'block' : 'none';
    }

    function toggleReplies(replyListId) {
        const replyList = document.getElementById(replyListId);

        // 현재 활성화된 답글 목록이 있다면 닫기
        if (activeReplyListId && activeReplyListId !== replyListId) {
            document.getElementById(activeReplyListId).style.display = 'none';
        }

        // 답글 목록 토글
        replyList.style.display = replyList.style.display === 'none' ? 'block' : 'none';

        // 활성화된 답글 목록 ID 업데이트
        activeReplyListId = replyList.style.display === 'block' ? replyListId : null;
    }
});