let progress = 0;

function showLoadingOverlay() {
    const loadingOverlay = document.getElementById('loadingOverlay');
    loadingOverlay.style.display = 'flex';
    progress = 0;
}

function hideLoadingOverlay() {
    const loadingOverlay = document.getElementById('loadingOverlay');
    loadingOverlay.style.display = 'none';
}

function updateProgressBar(progress) {
    const progressBar = document.getElementById('progressBar');
    progressBar.style.width = progress + '%';
}

function setProgressBarColor(color) {
    const progressBar = document.getElementById('progressBar');
    progressBar.style.backgroundColor = color;
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

function getOverlayInterval()
{

    let interval;
    return interval = setInterval(() => {
        if (progress < 90) { // 90%까지 증가
            progress += 5; // 5%씩 증가
            progressBar.style.width = progress + '%';
        }
    }, 500); // 100ms마다 증가;
}

function setFullProgressBar()
{
    progress = 100;
    progressBar.style.width = progress + '%';
}