const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

const imageMap = new Map();
let savedRange = null;
const editor = document.getElementById('editor');

editor.addEventListener('keyup', saveSelection);
editor.addEventListener('mouseup', saveSelection);

function saveSelection() {
    const sel = window.getSelection();
    if (sel.rangeCount) savedRange = sel.getRangeAt(0).cloneRange();
}
function restoreSelection() {
    if (savedRange) {
        const sel = window.getSelection(); sel.removeAllRanges(); sel.addRange(savedRange);
    }
}
function exec(cmd) { document.execCommand(cmd, false, null); saveSelection(); editor.focus(); }
function generateUUID() { return 'xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx'.replace(/[xy]/g, c => { const r = (Math.random()*16)|0; const v = c==='x'? r: (r&0x3)|0x8; return v.toString(16); }); }

function handleImageUpload(e) {
    const file = e.target.files[0];
    if (!file) return;

    const reader = new FileReader();
    reader.onload = () => {
        restoreSelection();
        const id = generateUUID();
        imageMap.set(id, file);

        const wrapper = document.createElement('span');
        wrapper.className = 'img-wrapper';

        const img = document.createElement('img');
        img.setAttribute('data-id', id);
        img.src = reader.result;

        const btn = document.createElement('button');
        btn.textContent = 'X';
        btn.className = 'delete-btn';
        btn.onclick = () => {
            wrapper.remove();
            imageMap.delete(id);
            saveSelection();
        };

        wrapper.append(img, btn);
        insertNodeAtCursor(wrapper);

        // !! 핵심: onload 안에서 실제 렌더 후 크기 측정 !!
        img.onload = () => {
            // 이 시점엔 레이아웃이 완료되어 올바른 값이 나옵니다
            const rect = img.getBoundingClientRect();
            img.style.width  = rect.width + 'px';
            img.style.height = rect.height + 'px';
        };

        saveSelection();
    };

    reader.readAsDataURL(file);
    e.target.value = '';
}

function insertNodeAtCursor(node) {
    const sel = window.getSelection(); if (!sel.rangeCount) return;
    const range = sel.getRangeAt(0); range.deleteContents(); range.insertNode(node);
    range.setStartAfter(node); range.collapse(true);
    sel.removeAllRanges(); sel.addRange(range); editor.focus();
}

function addCodeBlock() {
    const code = prompt('코드 입력:', ''); if (code!=null) { restoreSelection(); const pre=document.createElement('pre'); pre.textContent=code; insertNodeAtCursor(pre); saveSelection(); }
}

function submitPost() {
    let htmlString = editor.innerHTML;
    // <img> 태그 전체의 src를 data-id 기반 URL로 교체
    const baseUrl = window.location.origin; // 웹사이트 도메인 주소
    // 1) delete-btn 클래스 붙은 버튼 삭제
    htmlString = htmlString.replace(
        /<button\b[^>]*\bclass=(?:"[^"]*\bdelete-btn\b[^"]*"|'[^']*\bdelete-btn\b[^']*')[^>]*>[\s\S]*?<\/button>/g,
        ''
    );

    // 2) span.img-wrapper 태그만 제거 (안의 img 는 그대로)
    htmlString = htmlString.replace(/<\/?span\b[^>]*class="img-wrapper"[^>]*>/g, '');

    // 3) data URL 형태의 src 속성 전부 제거
    htmlString = htmlString.replace(/\s?src="data:[^"]*"/g, '');

    // 4) data-id 있는 img 태그만 골라서 src 속성 교체
    htmlString = htmlString.replace(
        /<img\b([^>]*?)\sdata-id="([^"]+)"([^>]*?)>/g,
        (_, preAttrs, id, postAttrs) =>
            `<img${preAttrs}${postAttrs} src="${baseUrl}/c/pic/${id}">`
    );

    // 5) 남은 data-id 속성 제거 (안전장치)
    htmlString = htmlString.replace(/\sdata-id="[^"]+"/g, '');


    // data-id 속성 제거

    console.log('서버 전송 HTML:', htmlString);
    console.log('서버 전송 이미지Map:', Array.from(imageMap.entries()));

    const formData = new FormData();

    for (const [id, file] of imageMap.entries()) {
        // field 이름을 모두 'files' 로 쓰되, 파일명엔 id를 붙이거나...
        formData.append('files', file, id + '_' + file.name);
        // 또는 'id' 별로 구분하고 싶으면:
        // formData.append(`file_${id}`, file);
    }

    const courseVO = {
        courseTitle:   document.getElementById('postTitle').value.trim(),
        courseType:    document.getElementById('lectureCategory').value,
        courseContent: htmlString
    };
    formData.append("courseVO", new Blob([JSON.stringify(courseVO)], { type: 'application/json' }));

    axios.post('/api/v1/courses/write', formData, {
            headers: {
                [csrfHeader]: csrfToken // CSRF 토큰을 헤더에 추가
            }
    })
        .then(response => {
        console.log('Success:', response.data);
    })
        .catch(error => {
            console.error('Error:', error);
        });
    alert('저장 완료');
}