package com.bookstore.two.refcursor;

import com.bookstore.dto.AuthorGenreResponse;

public interface AuthorRepositoryCustom {
    AuthorGenreResponse fetchNicknameGenreAndAgeByGenre(String genre);
    AuthorGenreResponse fetchNicknameGenreAndAgeByGenreFunc(String genre);
}
