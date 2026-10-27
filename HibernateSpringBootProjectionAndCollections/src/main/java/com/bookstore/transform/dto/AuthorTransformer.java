package com.bookstore.transform.dto;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class AuthorTransformer {

    public List<AuthorSummary> transform(List<Object[]> rs) {

        final Map<Long, AuthorSummary> authorsSummaryMap = new HashMap<>();

        for (Object[] o : rs) {
            
            Long authorId = ((Number) o[0]).longValue();

            AuthorSummary authorSummary = authorsSummaryMap.get(authorId);
            if (authorSummary == null) {
                authorSummary = new AuthorSummary();
                authorSummary.setId(((Number) o[0]).longValue());
                authorSummary.setName((String) o[1]);
                authorSummary.setGenre((String) o[2]);
            }

            BookSummary bookSummary = new BookSummary();
            bookSummary.setId(((Number) o[3]).longValue());
            bookSummary.setTitle((String) o[4]);

            authorSummary.addBook(bookSummary);
            authorsSummaryMap.putIfAbsent(authorSummary.getId(), authorSummary);
        }

        return new ArrayList<>(authorsSummaryMap.values());
    }
}
