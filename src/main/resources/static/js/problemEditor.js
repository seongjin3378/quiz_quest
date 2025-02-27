// Add new example input
const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');

document.getElementById('addExampleInput').addEventListener('click', () => {
    const newInputGroup = document.querySelector('.example-input-group').cloneNode(true);
    newInputGroup.querySelectorAll('input[type="text"]').forEach(input => input.value = '');
    newInputGroup.querySelector('input[type="checkbox"]').checked = false; // 새로운 체크박스는 해제 상태로 생성
    newInputGroup.querySelector('.btn-remove').addEventListener('click', removeExampleInput);
    document.getElementById('exampleInputsContainer').appendChild(newInputGroup);

    updateExampleCount(); // 추가된 부분: 개수 업데이트
});

// Remove example input
function removeExampleInput(event) {
    const inputGroup = event.target.closest('.example-input-group');
    if (inputGroup && document.querySelectorAll('.example-input-group').length > 2) {
        inputGroup.remove();
        updateExampleCount(); // 추가된 부분: 개수 업데이트
    } else {
        alert("최소 두개의 예시 입력은 남겨야 합니다.");
    }
}

// 추가된 부분: 예시 입력 개수 업데이트 함수
function updateExampleCount() {
    const count = document.querySelectorAll('.example-input-group').length;
    const maxCount = 10;
    document.getElementById('exampleCount').textContent = `(${count}/${maxCount})`;
}



function removeTrailingEnter(input) {
    while (input.endsWith("\n")) {
        input = input.slice(0, -1); // 마지막 문자를 제거
    }
    return input;
}


function getFormDataFromSaveRequest() {
    const formData = new FormData();
    let inputText = document.getElementById("code-area").value;
    const blob = new Blob([inputText], {type: 'text/plain'});
    const exampleInputItem = Array.from(document.querySelectorAll(".example-input-item"))
        .map(input => input.value);
    const resultInputItem = Array.from(document.querySelectorAll(".result-input-item")).map(input => input.value);
    const formCheckInput = Array.from(document.querySelectorAll(".form-check-input")).map(checkbox => checkbox.checked);
    const testCases = [];
    exampleInputItem.forEach((item, index) => {
        const testCasesVO = {
            testCaseId: -1,
            inputValue: removeTrailingEnter(item.replaceAll("↵", "\n")),
            outputValue: removeTrailingEnter(resultInputItem[index].replaceAll("↵", "\n")),
            isVisible: formCheckInput[index] ? 1 : 0, // 1 for visible, 0 for not visible
            problemId: -1
        };
        testCases.push(testCasesVO);
        console.log(testCasesVO);
    });

    const probExecutionVO = {
        memoryLimit: document.getElementById('memory-limit').value,
        timeLimit: document.getElementById('time-limit').value,
        problemTitle: document.getElementById('problem-title').value,
        problemType: document.getElementById('problem-type').value,
        problemContent: document.getElementById('problem-content').value,
        testCases: testCases
    };

    const uploadedImageCaptions = Array.from(document.querySelectorAll(".img-caption"))
        .map(input => input.value) // 각 input의 value를 추출하여 배열로 만듭니다.
        .filter(value => value); // 빈 값이 아닌 것만 필터링합니다.

    const probVisualVO = {
        visualCaptions : uploadedImageCaptions,
        visualTables : JSON.stringify(uploadedTables)
    }
    formData.append('probExecutionVO', new Blob([JSON.stringify(probExecutionVO)], { type: 'application/json' }));
    formData.append('file', blob, "file.py");
    formData.append('probVisualVO', new Blob([JSON.stringify(probVisualVO)], { type: 'application/json' }))
    uploadedImages.forEach(item => {
        formData.append('files', item);
    });
    return formData;
}

function saveRequest() {
    const formData = getFormDataFromSaveRequest();

    const language = document.getElementById("language").value;
    console.log(language);
    const url = '/admin/api/v1/problem/' + language + '/upload-and-validate';
    console.log(url);

    showLoadingOverlay();
    const interval = getOverlayInterval();

    axios.post(url, formData, {
        headers: {
            'Content-Type': 'multipart/form-data', // 멀티파트 전송을 위한 헤더
            [csrfHeader]: csrfToken
        }
    })
        .then(response => {
            // 성공적으로 응답을 받았을 때
            console.log('응답 데이터:', response.data);
            setFullProgressBar();
            clearInterval(interval); // 프로그레스 증가 중단
            setTimeout(() => {
                hideLoadingOverlay();
            }, 500); // 0.5초 후에 숨김
            if (response.data === "Yes") {
                setProgressBarColor('#25FF00');
                updateExperienceBar();
            } else {
                setProgressBarColor('#D32F2F');
            }
        })
        .catch(error => {
            // 에러가 발생했을 때
            const errorResponse = error.response.data;
            showError(errorResponse);
            setFullProgressBar();
            clearInterval(interval); // 프로그레스 증가 중단
            setProgressBarColor('#D32F2F');
        });
}


// Add event listener to existing remove buttons
document.querySelectorAll('.btn-remove').forEach(button => {
    button.addEventListener('click', removeExampleInput);
});

document.getElementById('addEnterCharacter').addEventListener('click', function() {
    // 클립보드에 '↵' 문자열을 복사하는 함수
    function copyToClipboard(text) {
        navigator.clipboard.writeText(text)
                .then(() => {
                    alert('클립보드에 복사되었습니다! CTRL+V를 입력하세요.');
                })
                .catch(err => {
                    alert.error('클립보드 복사 실패:', err);
                });
    }

    // '↵' 문자열을 클립보드에 복사
    copyToClipboard('↵');

    // 알림 표시
});


let uploadedImages = [];
let currentTableContainer = null;

function triggerImageUpload() {
    const inputFile = document.createElement('input');
    inputFile.type = 'file';
    inputFile.accept = 'image/*';
    inputFile.style.display = 'none';
    inputFile.onchange = function(event) {
        previewImage(event);
    };
    document.body.appendChild(inputFile);
    inputFile.click();
    document.body.removeChild(inputFile);
}

function previewImage(event) {
    const file = event.target.files[0];
    const reader = new FileReader();
    reader.onload = function(e) {
        const container = document.createElement('div');
        container.className = 'image-preview-container';

        const img = document.createElement('img');
        img.src = e.target.result;
        img.style.width = '200px';
        img.alt = '첨부된 이미지';

        // 캡션 입력 필드 추가
        const captionInput = document.createElement('input');
        captionInput.type = 'text';
        captionInput.className = 'form-control dark-form-control mt-2 img-caption';
        captionInput.placeholder = '이미지 캡션 입력';

        const removeBtn = document.createElement('button');
        removeBtn.textContent = '×';
        removeBtn.className = 'remove-btn';
        removeBtn.onclick = function() {
            container.remove();
            uploadedImages = uploadedImages.filter(item => item !== file);
        };

        container.appendChild(img);
        container.appendChild(captionInput); // 캡션 입력 필드 추가
        container.appendChild(removeBtn);
        document.getElementById('imagePreviewSection').appendChild(container);
        uploadedImages.push(file);
    };
    reader.readAsDataURL(file);
}

function showTableDialog() {
    const modal = new bootstrap.Modal(document.getElementById('tableModal'));
    modal.show();
}

function createTableFromDialog() {
    const rowCount = document.getElementById('rowCount').value;
    const colCount = document.getElementById('colCount').value;
    if (rowCount && colCount) {
        const tableContainer = document.getElementById('tableContainer');
        tableContainer.innerHTML = '';
        let tableHTML = '<table class="table editable-table"><thead><tr>';
        for (let i = 0; i < colCount; i++) {
            tableHTML += `<th>열 ${i + 1}</th>`;
        }
        tableHTML += '</tr></thead><tbody>';
        for (let j = 0; j < rowCount; j++) {
            tableHTML += '<tr>';
            for (let i = 0; i < colCount; i++) {
                tableHTML += `<td>행 ${j + 1} 열 ${i + 1}</td>`;
            }
            tableHTML += '</tr>';
        }
        tableHTML += '</tbody></table>';
        tableContainer.innerHTML = tableHTML;

        const cells = tableContainer.querySelectorAll('td, th');
        cells.forEach(cell => {
            cell.addEventListener('click', function() {
                if (cell.querySelector('input')) return;
                const currentText = cell.textContent;
                const input = document.createElement('input');
                input.value = currentText;
                input.onblur = function() {
                    cell.textContent = input.value;
                    input.remove();
                };
                input.onkeypress = function(e) {
                    if (e.key === 'Enter') {
                        cell.textContent = input.value;
                        input.remove();
                    }
                };
                cell.textContent = '';
                cell.appendChild(input);
                input.focus();
            });
        });
        currentTableContainer = tableContainer;
    }
}


let uploadedTables = []; // 표 데이터를 저장하는 전역 배열

function saveTableDataAsJson(table) {
    const captionInput = document.getElementById('tableCaption');
    const captionText = captionInput.value;

    // 표 데이터 객체 생성 (ID 없이)
    const tableData = {
        caption: captionText,
        rows: []
    };

    // 표의 행과 셀 데이터 추출
    const rows = table.querySelectorAll('tr');
    rows.forEach(row => {
        const rowData = [];
        const cells = row.querySelectorAll('td, th');
        cells.forEach(cell => {
            rowData.push(cell.textContent);
        });
        tableData.rows.push(rowData);
    });

    uploadedTables.push(tableData); // 배열에 추가
    return uploadedTables.length - 1; // 저장된 인덱스 반환
}


function saveTable() {
    if (currentTableContainer) {
        const table = currentTableContainer.querySelector('table');
        const tableIndex = saveTableDataAsJson(table); // 표 데이터를 저장하고 인덱스 받기

        // 미리보기 섹션 표시
        const previewSection = document.getElementById('previewSection');
        const previewContent = document.getElementById('previewContent');
        previewSection.style.display = 'block';

        // 미리보기 컨테이너 생성
        const tablePreviewContainer = document.createElement('div');
        tablePreviewContainer.className = 'table-preview-container';
        tablePreviewContainer.setAttribute('data-table-index', tableIndex); // 인덱스 저장

        // 표 복사 및 캡션 추가
        const clonedTable = table.cloneNode(true);
        const captionElement = document.createElement('p');
        captionElement.textContent = document.getElementById('tableCaption').value || '캡션 없음';
        captionElement.style.textAlign = 'center';
        captionElement.style.fontStyle = 'italic';

        // 삭제 버튼 생성
        const removeBtn = document.createElement('button');
        removeBtn.textContent = '×';
        removeBtn.className = 'remove-btn';
        removeBtn.onclick = function() {
            const index = parseInt(tablePreviewContainer.getAttribute('data-table-index'), 10);
                // 표가 하나 이상일 때만 삭제
                uploadedTables.splice(index, 1); // 배열에서 해당 데이터 제거
                tablePreviewContainer.remove(); // DOM에서 미리보기 제거

                // 남은 미리보기 컨테이너의 인덱스 업데이트
                const remainingContainers = previewContent.querySelectorAll('.table-preview-container');
                remainingContainers.forEach((container, newIndex) => {
                    container.setAttribute('data-table-index', newIndex);
                });
        };

        // 미리보기 컨테이너에 요소 추가
        tablePreviewContainer.appendChild(clonedTable);
        tablePreviewContainer.appendChild(captionElement);
        tablePreviewContainer.appendChild(removeBtn);
        previewContent.appendChild(tablePreviewContainer);

        // 모달 닫기
        const modal = bootstrap.Modal.getInstance(document.getElementById('tableModal'));
        modal.hide();
    }
}
