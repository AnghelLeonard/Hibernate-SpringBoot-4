package com.bookstore.service;

import com.bookstore.dto.AuthorDto;
import com.bookstore.dto.KeysetDto;
import com.bookstore.repository.AuthorRepository;
import com.bookstore.utils.Cursor;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }      
    
   public KeysetDto<AuthorDto> fetchNextPageOfAuthors(String cursorToken, int size) {
       
        // decode the opaque token into raw database fields
        Cursor.DecodedCursor cursor = Cursor.decode(cursorToken);

        // query the database
        List<AuthorDto> products = authorRepository.fetchNextPage(cursor.createdAt(), cursor.id(), (size+1));

        boolean hasNext = products.size() == (size +1);
        products.remove(products.size() - 1);
        
        String nextCursorToken = null;

        if (hasNext) {            
            // extract the last item and encode its fields into a new opaque token
            AuthorDto lastItem = products.get(products.size() - 1);
            nextCursorToken = Cursor.encode(lastItem.getCreatedAt(), lastItem.getId());
        }

        return new KeysetDto<>(products, nextCursorToken, hasNext);
    }
}

