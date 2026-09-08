package com.bookstore.view;

import com.blazebit.persistence.view.EntityView;
import static com.blazebit.persistence.view.FetchStrategy.MULTISET;
import com.blazebit.persistence.view.IdMapping;
import com.blazebit.persistence.view.Mapping;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import com.bookstore.entity.Publisher;
import com.bookstore.entity.Review;
import com.bookstore.entity.Reviewer;
import com.bookstore.entity.Tag;
import java.util.List;

@EntityView(Author.class)
public interface AuthorView {

    @IdMapping
    Long getId();

    String getName();
    String getGenre();

    int getAge();

    @Mapping(fetch = MULTISET)
    List<BookView> getBooks();

    @Mapping(fetch = MULTISET)
    List<TagView> getTags();

    @EntityView(Book.class)
    interface BookView {

        @IdMapping
        Long getId();

        String getTitle();
        String getIsbn();

        @Mapping(fetch = MULTISET)
        List<ReviewView> getReviews();

        @Mapping(fetch = MULTISET)
        PublisherView getPublisher();

        @EntityView(Review.class)
        interface ReviewView {

            @IdMapping
            Long getId();

            String getScript();
            String getLanguage();

            @Mapping(fetch = MULTISET)
            List<ReviewerView> getReviewers();

            @EntityView(Reviewer.class)
            interface ReviewerView {

                @IdMapping
                Long getId();

                String getReviewerName();
                int getReviewerAge();
            }
        }

        @EntityView(Publisher.class)
        interface PublisherView {

            @IdMapping
            Long getId();

            String getCompany();
            String getAddress();
        }
    }

    @EntityView(Tag.class)
    interface TagView {

        @IdMapping
        Long getId();

        String getTag();
    }
}
