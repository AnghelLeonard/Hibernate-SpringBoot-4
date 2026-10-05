package com.bookstore.service;

import com.bookstore.dto.WindowDto;
import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.OffsetScrollPosition;
import org.springframework.data.domain.ScrollPosition;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Window;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    
     public WindowDto<Author> fetchNextPageOfAuthors(Long offset, int size) {
         
        Sort sort = Sort.by(Sort.Direction.DESC, "id");        
        Limit limit = Limit.of(size);
        
        // Initialize or resume the offset position
        // If offset parameter is null, it defaults to 0 (the beginning)
        ScrollPosition position = (offset != null) 
                ? ScrollPosition.offset(offset) 
                : ScrollPosition.offset(); // initial offset = 0
        
         // Query the database window
        Window<Author> window = authorRepository.findBy(position, sort, limit);

        // Extract the next offset value for the client
        Long nextOffset = null;
        if (window.hasNext() && !window.isEmpty()) {
            // Extract position of the last element in the window
            OffsetScrollPosition lastPosition = (OffsetScrollPosition) window.positionAt(window.size() - 1);                        
            nextOffset = lastPosition.getOffset();
        }

        return new WindowDto<>(window.getContent(), String.valueOf(nextOffset), window.hasNext());
    }
}
