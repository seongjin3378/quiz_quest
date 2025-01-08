const axios = window.axios;
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');





(function () {
    function increaseTimer() {

        console.log("1: ", csrfToken, "2:", csrfHeader)
        const timerInterval = setInterval(() => {
            let timer = document.getElementById('usageTime');
            let increasedTimer = parseInt(timer.textContent, 10) + 1;
            timer.textContent = increasedTimer.toString();
            if (increasedTimer % 60 === 0) {
                console.log("인크리즈 타이머 실행 : ");
                const url = "api/v1/rankings/usage-timers";
                const UserUsageTimerVO = {
                    userUsageTimer: increasedTimer,
                };


                //for (let i = 0; i < 2; i++) { //TooManyRequest 테스트를 위해 작성
                    axios.put(url, UserUsageTimerVO, {
                        headers: {
                            [csrfHeader]: csrfToken // CSRF 토큰을 헤더에 추가
                        }
                    })

                        .then(() => {
                            console.log("메시지가 잘보내졌습니다. : " + UserUsageTimerVO);
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

function showContent(contentType) {
    const contentDiv = document.getElementById('content');
    contentDiv.style.display = 'flex'; // 영역 보이기
    contentDiv.innerHTML = '';


    // 콘텐츠에 따라 다른 텍스트 추가
    switch (contentType) {
        case 'problem':
            contentDiv.innerHTML += '<p class="card-text">문제 목록이 여기에 표시됩니다.</p>';
            break;
        case 'myProblem':
            contentDiv.innerHTML += '<p class="card-text">내 문제가 여기에 표시됩니다.</p>';
            break;
        case 'submit':
            contentDiv.innerHTML += '<p class="card-text">풀이 제출 영역입니다.</p>';
            break;
        case 'ranking':
            contentDiv.innerHTML += '<p class="card-text">전체 랭킹이 여기에 표시됩니다.</p>';
            break;
        case 'weeklyRanking':
            contentDiv.innerHTML += '<p class="card-text">주간 랭킹이 여기에 표시됩니다.</p>';
            break;
        case 'myRanking':
            contentDiv.innerHTML += '<p class="card-text">내 랭킹이 여기에 표시됩니다.</p>';
            break;
        case 'board':
            contentDiv.innerHTML += '<p class="card-text">전체 게시글이 여기에 표시됩니다.</p>';
            break;
        case 'myBoard':
            contentDiv.innerHTML += '<p class="card-text">내 게시글이 여기에 표시됩니다.</p>';
            break;
        case 'writeBoard':
            contentDiv.innerHTML += '<p class="card-text">게시글 작성 영역입니다.</p>';
            break;
        case 'lecture':
            contentDiv.innerHTML += '<p class="card-text">전체 강의가 여기에 표시됩니다.</p>';
            break;
        case 'myLecture':
            contentDiv.innerHTML += '<p class="card-text">내 강의가 여기에 표시됩니다.</p>';
            break;
        case 'registerLecture':
            contentDiv.innerHTML += '<p class="card-text">강의 등록 영역입니다.</p>';
            break;
        case 'questions':
            contentDiv.innerHTML += '<p class="card-text">질문 목록이 여기에 표시됩니다.</p>';
            break;
        case 'myQuestions':
            contentDiv.innerHTML += '<p class="card-text">내 질문이 여기에 표시됩니다.</p>';
            break;
        case 'writeQuestion':
            contentDiv.innerHTML += '<p class="card-text">질문 작성 영역입니다.</p>';
            break;
        case 'inquiries':
            contentDiv.innerHTML += '<p class="card-text">문의 목록이 여기에 표시됩니다.</p>';
            break;
        case 'myInquiries':
            contentDiv.innerHTML += '<p class="card-text">내 문의가 여기에 표시됩니다.</p>';
            break;
        case 'writeInquiry':
            contentDiv.innerHTML += '<p class="card-text">문의 작성 영역입니다.</p>';
            break;
    }

}
