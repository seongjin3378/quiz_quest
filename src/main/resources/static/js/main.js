const axios = window.axios;







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
