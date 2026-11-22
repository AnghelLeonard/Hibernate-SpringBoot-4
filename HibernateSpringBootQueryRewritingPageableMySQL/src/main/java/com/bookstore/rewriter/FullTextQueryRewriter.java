package com.bookstore.rewriter;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.QueryRewriter;
import org.springframework.stereotype.Component;

@Component
public class FullTextQueryRewriter implements QueryRewriter {

    public enum SearchMode {
        NATURAL_LANGUAGE("IN NATURAL LANGUAGE MODE"),
        BOOLEAN("IN BOOLEAN MODE");

        private final String sqlKeyword;

        SearchMode(String sqlKeyword) {
            this.sqlKeyword = sqlKeyword;
        }

        public String getSqlKeyword() {
            return sqlKeyword;
        }
    }
 
    private static final ThreadLocal<SearchMode> currentMode
            = ThreadLocal.withInitial(() -> SearchMode.NATURAL_LANGUAGE);

    public static void setMode(SearchMode mode) {
        currentMode.set(mode);
    }

    public static void clear() {
        currentMode.remove();
    }

    public static SearchMode getMode() {
        return currentMode.get();
    }
    
    @Override
    public String rewrite(String query, Sort sort) {
        return query.replace("_SEARCH_MODE_", currentMode.get().getSqlKeyword());
    }
}