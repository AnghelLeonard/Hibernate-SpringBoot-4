package com.bookstore.service;

import com.bookstore.entity.AuthorAssigned;
import com.bookstore.entity.AuthorAuto;
import com.bookstore.entity.AuthorIdentity;
import com.bookstore.entity.AuthorPersistable;
import com.bookstore.entity.AuthorVersion;
import org.springframework.stereotype.Service;
import com.bookstore.repository.AuthorAutoRepository;
import com.bookstore.repository.AuthorAssignedRepository;
import com.bookstore.repository.AuthorIdentityRepository;
import com.bookstore.repository.AuthorPersistableRepository;
import com.bookstore.repository.AuthorVersionRepository;

@Service
public class BookstoreService {

    private final AuthorAssignedRepository authorAssignedRepository;
    private final AuthorAutoRepository authorAutoRepository;
    private final AuthorIdentityRepository authorIdentityRepository;
    private final AuthorPersistableRepository authorPersistableRepository;
    private final AuthorVersionRepository authorVersionRepository;

    public BookstoreService(AuthorAssignedRepository authorAssignedRepository, 
            AuthorAutoRepository authorAutoRepository,
            AuthorIdentityRepository authorIdentityRepository,
            AuthorPersistableRepository authorPersistableRepository,
            AuthorVersionRepository authorVersionRepository) {
        this.authorAssignedRepository = authorAssignedRepository;
        this.authorAutoRepository = authorAutoRepository;
        this.authorIdentityRepository = authorIdentityRepository;
        this.authorPersistableRepository = authorPersistableRepository;
        this.authorVersionRepository = authorVersionRepository;
    }
    
    public void persistIdentityAuthor() {

        AuthorIdentity ai = new AuthorIdentity();       
        ai.setName("Joana Nimar");
        ai.setAge(34);
        ai.setGenre("History");

        authorIdentityRepository.save(ai);
    }
    
    public void persistPersistableAuthor() {

        AuthorPersistable ap = new AuthorPersistable();     
        ap.setId(2L);
        ap.setName("Jan Ulmy");
        ap.setAge(45);
        ap.setGenre("Anthology");

        authorPersistableRepository.save(ap);
    }
    
    public void persistVersionAuthor() {

        AuthorVersion av = new AuthorVersion();     
        av.setId(3L);
        av.setName("Goya Toot");
        av.setAge(31);
        av.setGenre("History");

        authorVersionRepository.save(av);
    }

    public void persistAssignedAuthor() {

        AuthorAssigned aa = new AuthorAssigned();
        aa.setId(1L);
        aa.setName("Markel Kils");
        aa.setAge(54);
        aa.setGenre("Anthology");

        authorAssignedRepository.save(aa);
    }

    public void persistAutoAuthor() {

        AuthorAuto ao = new AuthorAuto();
        ao.setName("Alicia Tom");
        ao.setAge(38);
        ao.setGenre("Anthology");

        authorAutoRepository.save(ao);
    }
}
