// ─── Utilities ───────────────────────────────────────────────────────────────

function getParam(key) {
    return new URLSearchParams(window.location.search).get(key);
}

function scoreColor(score) {
    if (!score) return 'secondary';
    if (score >= 8)  return 'success';
    if (score >= 6)  return 'warning';
    return 'danger';
}

function animeCard(a) {
    const img   = a.imageUrl || 'https://via.placeholder.com/100x140?text=No+Image';
    const score = a.score ? `<span class="badge bg-${scoreColor(a.score)}">${a.score}</span>` : '';
    const type  = a.type  ? `<span class="badge bg-light text-dark border">${a.type}</span>` : '';
    return `
        <div class="col">
            <a href="/pages/anime-detail.html?id=${a.malId}" class="text-decoration-none text-dark">
                <div class="card h-100 shadow-sm anime-card">
                    <img src="${img}" class="card-img-top anime-thumb" alt="${a.title}" onerror="this.src='https://via.placeholder.com/100x140?text=No+Image'">
                    <div class="card-body p-2">
                        <p class="card-title small fw-semibold mb-1 text-truncate" title="${a.title}">${a.title}</p>
                        <div class="d-flex gap-1 flex-wrap">${score}${type}</div>
                    </div>
                </div>
            </a>
        </div>`;
}

function characterCard(c) {
    const img = c.imageUrl || 'https://via.placeholder.com/80x110?text=?';
    return `
        <div class="col">
            <a href="/pages/character-detail.html?id=${c.characterMalId}" class="text-decoration-none text-dark">
                <div class="card h-100 shadow-sm">
                    <img src="${img}" class="card-img-top anime-thumb" alt="${c.name}" onerror="this.src='https://via.placeholder.com/80x110?text=?'">
                    <div class="card-body p-2">
                        <p class="small fw-semibold mb-0 text-truncate">${c.name}</p>
                        <p class="small text-muted mb-0">♥ ${(c.favorites||0).toLocaleString()}</p>
                    </div>
                </div>
            </a>
        </div>`;
}

function renderPagination(containerId, currentPage, totalPages, onPageChange) {
    const container = document.getElementById(containerId);
    if (!container || totalPages <= 1) return;
    let html = '<nav><ul class="pagination pagination-sm justify-content-center flex-wrap">';
    html += `<li class="page-item ${currentPage===0?'disabled':''}">
        <a class="page-link" href="#" data-page="${currentPage-1}">‹</a></li>`;
    const start = Math.max(0, currentPage - 2);
    const end   = Math.min(totalPages - 1, currentPage + 2);
    for (let i = start; i <= end; i++) {
        html += `<li class="page-item ${i===currentPage?'active':''}">
            <a class="page-link" href="#" data-page="${i}">${i+1}</a></li>`;
    }
    html += `<li class="page-item ${currentPage===totalPages-1?'disabled':''}">
        <a class="page-link" href="#" data-page="${currentPage+1}">›</a></li>`;
    html += '</ul></nav>';
    container.innerHTML = html;
    container.querySelectorAll('[data-page]').forEach(el => {
        el.addEventListener('click', e => {
            e.preventDefault();
            onPageChange(parseInt(el.dataset.page));
        });
    });
}

function showError(containerId, msg) {
    document.getElementById(containerId).innerHTML =
        `<div class="alert alert-danger">${msg}</div>`;
}

function showLoading(containerId) {
    document.getElementById(containerId).innerHTML =
        `<div class="text-center py-5"><div class="spinner-border text-secondary"></div></div>`;
}