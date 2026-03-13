const API = 'http://localhost:3000/api';

async function get(url) {
    const res = await fetch(url);
    if (!res.ok) throw new Error(`HTTP ${res.status}`);
    return res.json();
}

const api = {
    // Anime
    getAnime:          (page=0, size=20)        => get(`${API}/anime?page=${page}&size=${size}`),
    getAnimeById:      (id)                      => get(`${API}/anime/${id}`),
    searchAnime:       (q, page=0, size=20)      => get(`${API}/anime/search?q=${encodeURIComponent(q)}&page=${page}&size=${size}`),
    getTopByScore:     (page=0, size=20)         => get(`${API}/anime/top/score?page=${page}&size=${size}`),
    getTopByPopularity:(page=0, size=20)         => get(`${API}/anime/top/popularity?page=${page}&size=${size}`),
    getByGenre:        (genre, page=0, size=20)  => get(`${API}/anime/genre/${encodeURIComponent(genre)}?page=${page}&size=${size}`),
    getByType:         (type, page=0, size=20)   => get(`${API}/anime/type/${encodeURIComponent(type)}?page=${page}&size=${size}`),
    getByYear:         (year, page=0, size=20)   => get(`${API}/anime/year/${year}?page=${page}&size=${size}`),
    getBySeason:       (season, year, page=0, size=20) => get(`${API}/anime/season?season=${season}&year=${year}&page=${page}&size=${size}`),
    getByStudio:       (studio, page=0, size=20) => get(`${API}/anime/studio/${encodeURIComponent(studio)}?page=${page}&size=${size}`),

    // Characters
    getCharacterById:  (id)                      => get(`${API}/characters/${id}`),
    searchCharacters:  (q, page=0, size=20)      => get(`${API}/characters/search?q=${encodeURIComponent(q)}&page=${page}&size=${size}`),
    getTopCharacters:  (page=0, size=20)         => get(`${API}/characters/top?page=${page}&size=${size}`),
    getCharactersByAnime: (animeId, page=0, size=20) => get(`${API}/characters/anime/${animeId}?page=${page}&size=${size}`),

    // People
    getPersonById:     (id)                      => get(`${API}/people/${id}`),
    searchPeople:      (q, page=0, size=20)      => get(`${API}/people/search?q=${encodeURIComponent(q)}&page=${page}&size=${size}`),
    getTopPeople:      (page=0, size=20)         => get(`${API}/people/top?page=${page}&size=${size}`),

    // Users
    getUserByUsername: (username)                => get(`${API}/users/${username}`),
    getUsersByLocation:(location, page=0, size=20) => get(`${API}/users/location/${encodeURIComponent(location)}?page=${page}&size=${size}`),

    // Recommendations
    getRecommendations:(malId)                   => get(`${API}/recommendations/${malId}`),
    getTopRecommended: (limit=20)                => get(`${API}/recommendations/top?limit=${limit}`),

    // Ratings
    getRatingStats:    (animeId)                 => get(`${API}/ratings/anime/${animeId}/stats`),
    getUserRatings:    (username, page=0, size=20) => get(`${API}/ratings/user/${username}?page=${page}&size=${size}`),

    // Favs
    getUserFavs:       (username)                => get(`${API}/favs/user/${username}`),
    getTopFavs:        (type='anime', limit=20)  => get(`${API}/favs/top?type=${type}&limit=${limit}`),
};