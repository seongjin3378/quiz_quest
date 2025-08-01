
const $$ = (sel, ctx = document) => ctx.querySelector(sel);
const escapeHtml = str => str.replace(/[&<>"']/g, m => ({ '&': '&amp;', '<': '&lt;', '>': '&gt;', '"': '&quot;', "'": '&#39;' })[m]);

const csrfToken  = $$('meta[name="_csrf"]').content;
const csrfHeader = $$('meta[name="_csrf_header"]').content;
const axiosCfg   = { headers: { [csrfHeader]: csrfToken } };


/**
 * Like / Dislike Manager
 * ----------------------------------------------------------------------- */
class Reactions {
    constructor() {
        this.state      = parseInt($$('#state-text').textContent, 10); // 1,0,-1
        this.likes      = parseInt($$('#likeCount').textContent, 10)   || 0;
        this.dislikes   = parseInt($$('#dislikeCount').textContent, 10)|| 0;
        this.likeBtn    = $$('#likeBtn');
        this.dislikeBtn = $$('#dislikeBtn');
        this.likeCntEl  = $$('#likeCount');
        this.dislikeCntEl = $$('#dislikeCount');

        this.bindEvents();
        this.render();
    }

    bindEvents() {
        this.likeBtn.addEventListener('click', ()  => this.handleClick( 1));
        this.dislikeBtn.addEventListener('click', () => this.handleClick(-1));
    }

    async handleClick(requested) {
        const newState = this.state === requested ? 0 : requested;
        try {
            const { data } = await axios.post(`/api/v1/courses/like/${courseId}/${newState}`, {}, axiosCfg);
            this.likes    = data.totalLikes;
            this.dislikes = data.totalDisLikes;
            this.state    = data.currentState;
        } catch (err) {
            console.error('Reaction update failed', err);
        } finally {
            this.render();
        }
    }

    render() {
        this.likeCntEl.textContent    = this.likes;
        this.dislikeCntEl.textContent = this.dislikes;

        this.likeCntEl.classList.toggle('active',    this.state === 1);
        this.dislikeCntEl.classList.toggle('active', this.state === -1);
    }
}

/**
 * Comments Manager – handles fetch, render, CRUD, infinite scroll
 * ----------------------------------------------------------------------- */
class Comments {
    constructor() {
        this.container   = $$('#commentsList');
        this.form        = $$('#commentForm');
        this.input       = $$('#commentInput');
        this.cursor      = 0;
        this.isLoading   = false;
        this.sort        = 'DESC';

        this.bindForm();
        this.bindScroll();
        // prime initial comments
        this.load();
    }

    bindForm() {
        this.form.addEventListener('submit', async e => {
            e.preventDefault();
            const text = this.input.value.trim();
            if (!text) return;

            const largestId = this.container.querySelector('[data-id]')?.dataset.id;
            const payload   = { courseId, commentContent: text, largestCommentId: largestId };

            try {
                const { data } = await axios.post(`/api/v1/courses/${this.sort}/comments`, payload, axiosCfg);
                this.appendComments(data[0]);
                this.input.value = '';
            } catch (err) {
                console.error('Comment submit failed', err);
            }
        });
    }

    bindScroll() {
        window.addEventListener('scroll', () => {
            if (this.isLoading) return;
            const nearBottom = window.innerHeight + window.scrollY >= document.body.offsetHeight - 300;
            if (nearBottom) this.load();

        });
    }

    async load() {
        this.isLoading = true;
        try {
            const url        = `/api/v1/courses/${courseId}/${this.cursor}/${this.sort}/comments`;
            const { data }   = await axios.post(url, {}, axiosCfg);
            const comments   = data[0] ?? [];
            this.appendComments(comments);
            this.cursor = comments.length ? comments[comments.length - 1].cursor : this.cursor;
        } catch (err) {
            console.error('Comment load failed', err);
        } finally {
            this.isLoading = false;
        }
    }

    appendComments(list) {
        list.forEach(c => this.container.appendChild(this.renderComment(c)));
    }

    renderComment(c) {
        const div = document.createElement('div');
        div.className   = 'comment';
        div.dataset.id  = c.commentId;
        div.innerHTML   = `
      <p class="text">${escapeHtml(c.commentContent)}</p>
      <small>${escapeHtml(c.author)} • ${new Date(c.createdAt).toLocaleString()}</small>
      <div class="actions">
        <button class="editBtn">수정</button>
        <button class="deleteBtn">삭제</button>
      </div>`;

        div.querySelector('.editBtn').addEventListener('click', () => this.startEdit(div, c));
        div.querySelector('.deleteBtn').addEventListener('click', () => this.deleteComment(c.commentId, div));
        return div;
    }

    async deleteComment(id, node) {
        if (!confirm('정말 삭제하시겠습니까?')) return;
        try {
            await axios.delete(`/comments/${id}`, axiosCfg);
            node.remove();
        } catch (err) {
            console.error('Delete failed', err);
        }
    }

    startEdit(container, original) {
        const textarea = document.createElement('textarea');
        textarea.className = 'editInput';
        textarea.value = original.commentContent;
        container.replaceChildren(textarea);

        const controls = document.createElement('div');
        controls.innerHTML = '<button class="saveBtn">저장</button><button class="cancelBtn">취소</button>';
        container.appendChild(controls);

        controls.querySelector('.saveBtn').addEventListener('click', () => this.saveEdit(container, original.commentId));
        controls.querySelector('.cancelBtn').addEventListener('click', () => {
            container.replaceWith(this.renderComment(original));
        });
    }

    async saveEdit(container, id) {
        const newText = container.querySelector('.editInput').value.trim();
        if (!newText) return alert('댓글을 입력하세요');
        try {
            const { data } = await axios.put(`/comments/${id}`, { text: newText }, axiosCfg);
            container.replaceWith(this.renderComment(data));
        } catch (err) {
            console.error('Edit failed', err);
        }
    }
}

/**
 * Bootstrapping once DOM is ready
 * ----------------------------------------------------------------------- */
document.addEventListener('DOMContentLoaded', () => {
    new Reactions();
    new Comments();
});
