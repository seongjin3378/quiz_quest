
let heartbeatInterval;

function sendHeartbeat() {
    const data = new URLSearchParams();
    data.append('key', 'value'); // 필요한 데이터 추가
    data.append(csrfHeader, csrfToken);

    // sendBeacon을 사용하여 하트비트 요청
    axios.post('/api/v1/users/current', data, {
        headers: {
            [csrfHeader]: csrfToken // CSRF 토큰을 헤더에 추가
        }
    })
        .then(response => {
            console.log('Success:', response.data);
            document.getElementById("currentUsers").textContent = response.data;
        })
        .catch(error => {
            console.error('Error:', error);
        });
}

// 페이지가 로드될 때 하트비트 시작
window.addEventListener('load', () => {
    // 최초 하트비트 요청
    console.log("최초 하트비트 요청");
    sendHeartbeat();

    // 59초마다 하트비트 요청 설정
    heartbeatInterval = setInterval(sendHeartbeat, 59000);
});
