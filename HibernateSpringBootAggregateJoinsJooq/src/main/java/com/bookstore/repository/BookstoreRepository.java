package com.bookstore.repository;

import com.bookstore.view.AuthorView;
import com.bookstore.view.BookView;
import com.bookstore.view.PublisherView;
import com.bookstore.view.ReviewView;
import com.bookstore.view.ReviewerView;
import com.bookstore.view.TagView;
import java.util.List;
import static jooq.generated.tables.Author.AUTHOR;
import org.jooq.DSLContext;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.field;
import static org.jooq.impl.DSL.multiset;
import static org.jooq.impl.DSL.row;
import static org.jooq.impl.DSL.select;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
@Transactional(readOnly = true)
public class BookstoreRepository {

    private final DSLContext ctx;

    public BookstoreRepository(DSLContext ctx) {
        this.ctx = ctx;
    }

    public List<AuthorView> fetchAuthorsBooksTagsPublishersReviewersReviews() {

        List<AuthorView> result = ctx.select(
                AUTHOR.ID, AUTHOR.NAME, AUTHOR.GENRE, AUTHOR.AGE,
                multiset(select(AUTHOR.tag().ID, AUTHOR.tag().TAG_).from(AUTHOR.tag()))
                        .as("tags").convertFrom(r -> r.map(mapping(TagView::new))),
                multiset(
                        select(AUTHOR.book().ID, AUTHOR.book().TITLE, AUTHOR.book().ISBN,
                                multiset(
                                        select(AUTHOR.book().review().ID, AUTHOR.book().review().SCRIPT, AUTHOR.book().review().LANGUAGE,
                                                multiset(
                                                        select(
                                                                AUTHOR.book().review().reviewer().ID,
                                                                AUTHOR.book().review().reviewer().REVIEWER_NAME,
                                                                AUTHOR.book().review().reviewer().REVIEWER_AGE)
                                                                .from(AUTHOR.book().review().reviewer())
                                                ).as("reviewers").convertFrom(r -> r.map(mapping(ReviewerView::new)))
                                        ).from(AUTHOR.book().review())
                                ).as("reviews").convertFrom(r -> r.map(mapping(ReviewView::new))),
                                field(select(row(
                                        AUTHOR.book().publisher().ID,
                                        AUTHOR.book().publisher().COMPANY,
                                        AUTHOR.book().publisher().ADDRESS
                                ).mapping(PublisherView::new).as("publisher")))
                        ).from(AUTHOR.book().publisher())
                ).as("books").convertFrom(r -> r.map(mapping(BookView::new)))
        ).from(AUTHOR)
                .orderBy(AUTHOR.NAME)
                .fetch(mapping(AuthorView::new));

        return result;
    }
}
