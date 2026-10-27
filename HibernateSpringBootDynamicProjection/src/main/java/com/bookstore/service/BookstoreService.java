package com.bookstore.service;

import com.bookstore.repository.AuthorRepository;
import org.springframework.stereotype.Service;
import com.bookstore.entity.Author;
import java.util.List;
import com.bookstore.dto.AuthorGenre;
import com.bookstore.dto.AuthorNameEmail;

@Service
public class BookstoreService {

    private final AuthorRepository authorRepository;

    public BookstoreService(AuthorRepository authorRepository) {

        this.authorRepository = authorRepository;
    }
    
    public void fetchAuthorByNameAsEntityJpql() {
        Author author = authorRepository.findByName("Joana Nimar", Author.class);

        System.out.println(author);
    }

    public void fetchAuthorByNameAsDtoNameEmailJpql() {
        AuthorNameEmail author = authorRepository.findByName("Joana Nimar", AuthorNameEmail.class);

        System.out.println(author.getEmail() + ", " + author.getName());
    }

    public void fetchAuthorByNameAsDtoGenreJpql() {
        AuthorGenre author = authorRepository.findByName("Joana Nimar", AuthorGenre.class);

        System.out.println(author.getGenre());
    }
    
    public void fetchAuthorByNameAndAgeAsEntityJpql() {
        Author author = authorRepository.findByNameAndAge("Joana Nimar", 34, Author.class);

        System.out.println(author);
    }

    public void fetchAuthorByNameAndAgeAsDtoNameEmailJpql() {
        AuthorNameEmail author = authorRepository.findByNameAndAge("Joana Nimar", 34, AuthorNameEmail.class);

        System.out.println(author.getEmail() + ", " + author.getName());
    }

    public void fetchAuthorByNameAndAgeAsDtoGenreJpql() {
        AuthorGenre author = authorRepository.findByNameAndAge("Joana Nimar", 34, AuthorGenre.class);

        System.out.println(author.getGenre());
    }
   
    public void fetchAuthorsAsEntitiesJpql() {
        List<Author> authors = authorRepository.findByGenre("Anthology", Author.class);

        System.out.println(authors);
    }

    public void fetchAuthorsAsDtoJpql() {
        List<AuthorNameEmail> authors = authorRepository.findByGenre("Anthology", AuthorNameEmail.class);

        authors.forEach(a -> System.out.println(a.getName() + ", " + a.getEmail()));
    }

}
