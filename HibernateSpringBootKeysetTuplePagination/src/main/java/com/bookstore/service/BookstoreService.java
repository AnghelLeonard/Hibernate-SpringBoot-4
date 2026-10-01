package com.bookstore.service;

import com.bookstore.dto.AuthorDto;
import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import java.time.Instant;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {
        this.authorRepository = authorRepository;
    }
    
    public void insertData() {
        
        for(int i = 0;i < 100; i++) {
            
            Author author = new Author();
            
            author.setAge(0);
            author.setGenre("Genre-" + i);
            author.setName("Name-" + i);
            
            authorRepository.save(author);
        }
        
        System.out.println("Done inserting ... try 'localhost:8080/authors'");
    }

    public List<Author> fetchNextPageOfAuthors(Instant ltime, Long lid, int size) {
        return authorRepository.fetchNextPage(ltime, lid, size);
    }
    
    public List<Author> fetchNextPageableOfAuthors(Instant ltime, Long lid, int size) {        
        Pageable pageable = PageRequest.of(0, size);
        
        return authorRepository.fetchNextPageable(ltime, lid, pageable);
    }
    
    public List<AuthorDto> fetchNextPageAsDtoOfAuthors(Instant ltime, Long lid, int size) {
        return authorRepository.fetchNextPageDto(ltime, lid, size);
    }
}
