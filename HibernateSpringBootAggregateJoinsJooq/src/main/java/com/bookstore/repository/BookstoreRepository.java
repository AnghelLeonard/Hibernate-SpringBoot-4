package com.bookstore.repository;

import com.bookstore.view.AuthorView;
import com.bookstore.view.BookView;
import com.bookstore.view.ReviewView;
import com.bookstore.view.ReviewerView;
import com.bookstore.view.TagView;
import java.util.List;
import static jooq.generated.tables.Author.AUTHOR;
import static jooq.generated.tables.AuthorTag.AUTHOR_TAG;
import static jooq.generated.tables.Book.BOOK;
import static jooq.generated.tables.Review.REVIEW;
import static jooq.generated.tables.ReviewReviewer.REVIEW_REVIEWER;
import static jooq.generated.tables.Reviewer.REVIEWER;
import static jooq.generated.tables.Tag.TAG;
import org.jooq.DSLContext;
import static org.jooq.Records.mapping;
import static org.jooq.impl.DSL.multiset;
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
      /*  List<AuthorView> result = ctx.select(
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
                .fetch(mapping(AuthorView::new));*/
      
      List<AuthorView> result = ctx.select(
                AUTHOR.ID, AUTHOR.NAME, AUTHOR.GENRE, AUTHOR.AGE,
                multiset(
                        select(TAG.ID, TAG.TAG_)
                                .from(TAG)
                                .leftJoin(AUTHOR_TAG)
                                .on(TAG.ID.eq(AUTHOR_TAG.TAG_ID))
                                .where(AUTHOR_TAG.AUTHOR_ID.eq(AUTHOR.ID))
                ).as("tags").convertFrom(r -> r.map(mapping(TagView::new))),
                multiset(
                        select(BOOK.ID, BOOK.TITLE, BOOK.ISBN,
                                multiset(
                                        select(REVIEW.ID, REVIEW.SCRIPT, REVIEW.LANGUAGE,
                                                multiset(
                                                        select(REVIEWER.ID, REVIEWER.REVIEWER_NAME, REVIEWER.REVIEWER_AGE)
                                                                .from(REVIEWER)
                                                                .leftJoin(REVIEW_REVIEWER)
                                                                .on(REVIEWER.ID.eq(REVIEW_REVIEWER.REVIEWER_ID))
                                                                .where(REVIEW_REVIEWER.REVIEW_ID.eq(REVIEW.ID))
                                                ).as("reviewers").convertFrom(r -> r.map(mapping(ReviewerView::new)))
                                        ).from(REVIEW)
                                        .where(REVIEW.BOOK_ID.eq(BOOK.ID))
                                ).as("reviews").convertFrom(r -> r.map(mapping(ReviewView::new)))
                        ).from(BOOK)
                        .where(BOOK.AUTHOR_ID.eq(AUTHOR.ID))
                ).as("books").convertFrom(r -> r.map(mapping(BookView::new)))
        ).from(AUTHOR)
          .orderBy(AUTHOR.NAME)
         .fetch(mapping(AuthorView::new));
        return result;
    }
}
