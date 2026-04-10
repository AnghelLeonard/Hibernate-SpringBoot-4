package com.bookstore.view;

import com.blazebit.persistence.view.EntityView;
import com.blazebit.persistence.view.IdMapping;
import com.bookstore.entity.Author;
import com.bookstore.entity.Book;
import java.util.Set;

@EntityView(Author.class)
public interface AuthorBookView {
    
    @IdMapping
    Long getId();
    String getName();
    Integer getAge();
    Set<BookView> getBooks();
    
    @EntityView(Book.class)
    interface BookView {
        
        @IdMapping
        Long getId();
        String getTitle();        
    }       
}
