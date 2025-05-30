let likes = 0;
let dislikes = 0;
const likeBtn = document.getElementById('likeBtn');
const dislikeBtn = document.getElementById('dislikeBtn');
const likeCount = document.getElementById('likeCount');
const dislikeCount = document.getElementById('dislikeCount');

likeBtn.addEventListener('click', () => {
    likes++;
    likeCount.textContent = likes;
});
dislikeBtn.addEventListener('click', () => {
    dislikes++;
    dislikeCount.textContent = dislikes;
});

const commentForm = document.getElementById('commentForm');
const commentInput = document.getElementById('commentInput');
const commentsList = document.getElementById('commentsList');

commentForm.addEventListener('submit', e => {
    e.preventDefault();
    const text = commentInput.value.trim();
    if (!text) return;
    const commentEl = document.createElement('div');
    commentEl.className = 'comment';
    commentEl.innerHTML = `
        <div class="comment-header">익명 | ${new Date().toLocaleString()}</div>
        <div class="comment-body">${text}</div>
      `;
    commentsList.prepend(commentEl);
    commentInput.value = '';
});