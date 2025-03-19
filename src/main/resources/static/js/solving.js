

const problemContentElement = document.getElementById("problem-content");
const problemContentText = problemContentElement.innerText.replaceAll("↵", "<br>");
problemContentElement.innerHTML = '';
problemContentElement.insertAdjacentHTML('beforeend', problemContentText);
const problemInputValue = document.querySelectorAll(".input-value");
const problemOutputValue = document.querySelectorAll(".output-value");



function inputOutputFormatter(element, searchValue, replaceValue)
{
    element.forEach((item) => {
        if(item.innerText.includes(searchValue)) {
            const itemText = item.innerText.replaceAll(searchValue, replaceValue);
            item.innerHTML = '';
            item.insertAdjacentHTML('beforeend', itemText);
        }
    });
}

inputOutputFormatter(problemInputValue, "↵", "<br>");
inputOutputFormatter(problemOutputValue, "↵", "<br>");


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




document.getElementById('closeButton').addEventListener('click', function() {
    const errorMessageDiv = document.getElementById('errorMessage');
    errorMessageDiv.style.display = 'none'; // 숨김
});


function checkAnswerRequest()
{
    let inputText = document.getElementById("code-area").value;
    const blob = new Blob([inputText], {type: 'text/plain'});
    const formData = new FormData();
    formData.append('file', blob, "file.py");
    const language = document.getElementById("combo-box").value;
    console.log(language);
    const url = '/api/v1/problems/'+problemIndexGlobal+'/'+language+'/upload-and-validate';
    console.log(url);

    showLoadingOverlay();
    const interval = getOverlayInterval();

    axios.post(url, formData, {
        headers: {
            [csrfHeader]: csrfToken, // CSRF 토큰을 헤더에 추가
            'Content-Type': 'multipart/form-data' // 멀티파트 전송을 위한 헤더
        }
    })
        .then(response => {
            // 성공적으로 응답을 받았을 때
            console.log('응답 데이터:', response.data);
            setFullProgressBar()
            clearInterval(interval); // 프로그레스 증가 중단
            setTimeout(() => {
                hideLoadingOverlay()
            }, 500); // 0.5초 후에 숨김
            if(response.data === "Yes")
            {
                // 로딩 오버레이 숨기기
                const myModal = new bootstrap.Modal(document.getElementById('myModal'));
                setProgressBarColor('#25FF00');
                myModal.show();
                updateExperienceBar()
            }else{
                setProgressBarColor('#D32F2F');
            }
        })
        .catch(error => {
            // 에러가 발생했을 때
            const errorResponse = error.response.data;
            showError(errorResponse);
            setFullProgressBar()
            clearInterval(interval); // 프로그레스 증가 중단
            setProgressBarColor('#D32F2F');
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


