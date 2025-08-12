/* comment-modal.js (v3.3 – Axios edition)
 * -------------------------------------------------------------
 * Axios를 사용하여 fetchJSON → axiosReq 로 교체했습니다.
 * 나머지 로직은 v3.2 와 동일합니다.
 * ------------------------------------------------------------- */

(() => {
    'use strict';

    /* ---------- 상태 ---------- */
    const state = {
        sort: 'DESC',
        cursor: '0',
        activeParent: null,
        activeToggleBtn: null,
        replyCursors: new Map(),
        commentIndex: 0, // init 에서 계산
        replyCommentIndex: 0
    };

    /* ---------- DOM ---------- */
    const els = {};

    /* ---------- 유틸 ---------- */
    const isBottom = (el) => el.scrollTop + el.clientHeight + 2 >= el.scrollHeight;

    const axiosReq = (url, method = 'GET', data = null) => {
        return axios({
            url,
            method,
            headers: { [csrfHeader]: csrfToken },
            data,
        }).then((r) => r.data);
    };


    /* ---------- API ---------- */
    const api = {
        comments: (c = '0') => axiosReq(`/api/v1/problems/${problemIndexGlobal}/${c}/${state.sort}/comments`),
        replies: (p, c = '0') => axiosReq(`/api/v1/problems/${p}/${c}/ASC/reply-comments`),
        post: (payload) => axiosReq(`/api/v1/problems/${state.sort}/comments`, 'POST', payload),
    }

    /* ---------- 템플릿 ---------- */
    const commentTpl = (c) => {
        const idx = state.commentIndex++;
        console.log(c);
        return `<div class="comment p-2 border-bottom" data-comment-id="${c.commentId}">
      <div class="comment-header small text-muted mb-1">
        <span>${c.author}</span> • <span>${c.createdAt}</span> • <span>LV ${c.level}</span>
      </div>
      <p class="m-0">${c.commentContent}</p>
      <div class="d-flex gap-2 mt-2">
        <button class="btn btn-link p-0" data-reply-open="${idx}">답글 달기</button>
        <button class="btn btn-link p-0" data-reply-toggle="${idx}" data-parent="${c.commentId}">답글 보기(${c.replyCount})</button>
        <button class="btn btn-link p-0 text-danger">신고</button>
      </div>
      <div class="reply-section mt-2" id="reply${idx}" style="display:none;">
        <textarea class="form-control mb-2" placeholder="답글을 입력하세요..."></textarea>
        <button class="btn btn-sm btn-primary" data-write-reply data-parent="${c.commentId}">답글 작성</button>
      </div>
    </div>`;
    };

    const replyTpl = (r) => {

        const idx = state.replyCommentIndex++;
        return `
   <div class="comment p-2 border-bottom">
      c
      <div class="comment-header small text-muted mb-1">
        <span>${r.author}</span> • <span>${r.createdAt}</span> • <span>LV ${r.level}</span>
      </div>
      <p class="m-0">${r.commentContent}</p>
      <div class="d-flex gap-2 mt-2">
        <button class="btn btn-link p-0" data-reply-Reply-open="${idx}" data-parent="${r.commentId}" data-receiver="${r.author}">답글 달기</button>
        <button class="btn btn-link p-0 text-danger">신고</button>
      </div>
        <div class="reply-section mt-2" id="replyReply${idx}" style="display:none;">
        <textarea class="form-control mb-2" placeholder="답글을 입력하세요..."></textarea>
        <button class="btn btn-sm btn-primary" data-write-reply data-parent="${r.parentCommentId}">답글 작성</button>
      </div>
    </div>`
    };

    /* ---------- 렌더 ---------- */
    const renderComments = (arr, before) => {
        if (!arr[0].length) return;
        const html = arr[0].map(commentTpl).join('');
        console.log(html);
        (before || els.commentSection).insertAdjacentHTML(before ? 'beforebegin' : 'beforeend', html);
        state.cursor = arr[0][arr[0].length - 1].cursor;
    };

    const renderReplies = (arr, parent) => {
        if (!arr[0].length) return;
        els.replyList.insertAdjacentHTML('beforeend', arr[0].map(replyTpl).join(''));
        state.replyCursors.set(parent, arr[0][arr[0].length - 1].cursor);
    };



    /* ---------- 데이터 로드 ---------- */
    const loadComments = async () => {
        if (state.cursor === null) return; // cursor가 null이면 실행 안 함
        renderComments(await api.comments(state.cursor));
    };

    const loadReplies = async (parent) => renderReplies(await api.replies(parent, state.replyCursors.get(parent) || '0'), parent);

    /* ---------- 작성 ---------- */
    const writeComment = async () => {
        const textarea = els.commentSection.querySelector('div.comment:first-child textarea');
        const largestCommentId = els.commentSection
                .querySelector('[data-comment-id]')?.dataset.commentId
            ?? '0';
        console.log(largestCommentId);

        if (!textarea.value.trim()) return;
        const newComments = await api.post({ commentContent: textarea.value, boardId: problemIndexGlobal, largestCommentId: largestCommentId });

        console.log(newComments);
        renderComments(newComments, els.commentSection.children[1]);

        textarea.value = '';
    };

    const writeReply = async (btn) => {
        const area = btn.previousElementSibling;
        const targetReceiverEl = btn.closest('[data-receiver]');

        let replyBtn = btn.parentElement.previousElementSibling;

        console.log(replyBtn)
        replyBtn = replyBtn.querySelector('[data-reply-toggle]')


        if (replyBtn) {
            replyBtn.textContent = replyBtn.textContent.replace(/\((\d+)\)/, (_, num) => {
                return `(${parseInt(num, 10) + 1})`;
            });
        }


        if (!area.value.trim()) return;

        const payload = {
            commentContent: btn.dataset.receiver ? `@${targetReceiverEl.dataset.receiver} ${area.value}` : area.value,
            parentCommentId: btn.dataset.parent,
            boardId: problemIndexGlobal,
        };

        console.log(payload);
        const resultReply = await api.post(payload);
        console.log(resultReply);
        area.value = '';
        //els.replyList.innerHTML = '';
        //state.replyCursors.set(btn.dataset.parent, '0');
        await loadReplies(btn.dataset.parent);
    };

    /* ---------- 이벤트 ---------- */
    const onClick = async (e) => {
        const t = e.target;
        if (t.matches('button.btn.btn-primary.btn-sm') && !t.matches('[data-write-reply]')) return writeComment();
        console.log(t);
        if (t.dataset.replyOpen !== undefined) {
            const sec = document.getElementById('reply' + t.dataset.replyOpen);
            if (sec) sec.style.display = sec.style.display === 'none' ? 'block' : 'none';
            return;
        }


        if (t.dataset.replyReplyOpen !== undefined) {
            const sec = document.getElementById('replyReply' + t.dataset.replyReplyOpen);
            if (sec) sec.style.display = sec.style.display === 'none' ? 'block' : 'none';
            return;
        }


        if (t.dataset.replyToggle !== undefined) { // 답글 보기 열었을 경우
            const parent = t.dataset.parent;


            if (t.textContent.trim() === '답글 보기(0)') {
                return;
            }
            els.replyList.style.display =
                els.replyList.style.display === 'block' ? 'none' : 'block';

            if (state.activeParent !== parent) {
                els.replyList.innerHTML = '';
                state.replyCursors.set(parent, '0');
            }
            state.activeParent = parent;
            await loadReplies(parent);
            return;
        }
        if (t.dataset.writeReply !== undefined) return writeReply(t);
    };

    const onScrollComments = () => { if (isBottom(els.commentSection)) loadComments(); };
    const onScrollReplies = () => { if (state.activeParent && isBottom(els.replyList)) loadReplies(state.activeParent); };

    /* ---------- 정렬 ---------- */
    const switchSort = async (s) => {
        if (state.sort === s) return;
        state.sort = s;
        els.sortBtn.textContent = s === 'DESC' ? '최신순' : '오래된 순';
        state.cursor = '0';
        state.activeParent = null;
        state.activeToggleBtn = null;
        const writer = els.commentSection.children[0].outerHTML;
        els.commentSection.innerHTML = writer;
        state.commentIndex = 1;
        await loadComments();
        els.replyList.innerHTML = '';
        els.replyList.style.display = 'none';
    };

    /* ---------- 초기화 ---------- */
    const init = () => {
        Object.assign(els, {
            commentSection: document.querySelector('.comment-section'),
            replyList: document.querySelector('.reply-list'),
            sortBtn: document.getElementById('sortButton'),
            sortLatest: document.getElementById('sortLatest'),
            sortOldest: document.getElementById('sortOldest'),
            cursorInput: document.getElementById('cursor'),
        });

        state.commentIndex = els.commentSection.querySelectorAll('.comment').length;

        document.addEventListener('click', onClick);
        els.commentSection.addEventListener('scroll', onScrollComments);
        els.replyList.addEventListener('scroll', onScrollReplies);
        els.sortLatest.addEventListener('click', () => switchSort('DESC'));
        els.sortOldest.addEventListener('click', () => switchSort('ASC'));


        console.log(1);
        loadComments();
    };

    document.addEventListener('DOMContentLoaded', init);
})();
