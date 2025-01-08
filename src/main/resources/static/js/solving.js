function toggleSidebar() {
    const sidebar = document.getElementById('sidebar');
    sidebar.style.right = sidebar.style.right === '0px' ? '-220px' : '0px';
}



function nextProblem() {
    alert("다음 문제로 이동합니다.");
}

function submitComment() {
    alert("댓글이 작성되었습니다.");
}

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
    axios.post(url, formData, {
        headers: {
            'Content-Type': 'multipart/form-data' // 멀티파트 전송을 위한 헤더
        }
    })
        .then(response => {
            // 성공적으로 응답을 받았을 때
            console.log('응답 데이터:', response.data);
        })
        .catch(error => {
            // 에러가 발생했을 때
            console.error('에러 발생:', error);
        });
}