package com.bookstore;

import com.bookstore.dto.AuthorGenreResponse;
import com.bookstore.dto.GenreComparisonDto;
import com.bookstore.dto.NicknameAndAgeDto;
import com.bookstore.dto.NicknameAndAgeDtoI;
import com.bookstore.entity.Author;
import com.bookstore.service.BookstoreService;
import java.util.List;
import java.util.Optional;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MainApplication {

    private final BookstoreService bookstoreService;

    public MainApplication(BookstoreService bookstoreService) {
        this.bookstoreService = bookstoreService;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {

            System.out.println("--------------------------");
            System.out.println("-------JdbcTemplate-------");
            System.out.println("--------------------------");

            System.out.println("Call 'fetch_author_by_genre()'");
            List<Author> authors1 = bookstoreService.fetchAnthologyAuthors();
            System.out.println(authors1);

            System.out.println("\n--------------------------");

            System.out.println("\nCall 'fetch_author_by_genre_func()'");
            List<Author> authors2 = bookstoreService.fetchFuncAnthologyAuthors();
            System.out.println(authors2);

            System.out.println("\n--------------------------");

            System.out.println("\nCall 'fetch_nickname_and_age_by_genre()'");
            List<NicknameAndAgeDto> authors3 = bookstoreService.fetchNicknameAndAgeByGenre();
            System.out.println(authors3);

            System.out.println("\n--------------------------");

            System.out.println("\nCall 'fetch_nickname_and_age_by_genre_func()'");
            List<NicknameAndAgeDto> authors4 = bookstoreService.fetchNicknameAndAgeByGenreFunc();
            System.out.println(authors4);

            System.out.println("\n--------------------------");

            System.out.println("\nCall 'fetch_nickname_genre_and_age_by_genre()'");
            AuthorGenreResponse authors5 = bookstoreService.fetchNicknameGenreAndAgeByGenre();
            System.out.println(authors5);

            System.out.println("\n--------------------------");

            System.out.println("\nCall 'fetch_nickname_and_age_by_id()'");
            NicknameAndAgeDto authors6 = bookstoreService.fetchNicknameAndAgeById();
            System.out.println(authors6);
            
            System.out.println("\nCall 'fetch_nickname_and_age_by_id_func()'");
            Optional<NicknameAndAgeDto> authors61 = bookstoreService.fetchNicknameAndAgeByIdFunc();
            System.out.println(authors61);
            
            System.out.println("\nCall 'fetch_nickname_genre_and_age_by_genre_two_cursors_func()'");
            GenreComparisonDto authors62 = bookstoreService.fetchNicknameGenreAndAgeByGenreTwoCursors();
            System.out.println(authors62);       
            
            System.out.println("\nCall 'fetch_nickname_genre_and_age_by_genre_table_func()'");
            GenreComparisonDto authors63 = bookstoreService.fetchNicknameGenreAndAgeByGenreTable();
            System.out.println(authors63);            

            System.out.println("\n--------------------------");
            System.out.println("--------@Procedure--------");
            System.out.println("--------------------------");
            
            System.out.println("\nCall 'fetch_nickname_and_age_by_id_func()'");
            NicknameAndAgeDtoI authors7 = bookstoreService.fetchNicknameAndAgeByIdRepo();            
            String nickname = authors7.getNickname(); // Calls TupleBackedMap under the hood
            Integer age = authors7.getAge();
            System.out.println("Nickname: " + nickname + ", Age: " + age);
            

            System.out.println("\nCall 'fetch_nickname_and_age_by_id()'");
            NicknameAndAgeDtoI authors8 = bookstoreService.fetchNicknameAndAgeByIdProc();
            System.out.println(authors8);
            
            System.out.println("\n--------------------------");

            System.out.println("\nCall 'fetch_nickname_and_age_by_genre()'");
            List<NicknameAndAgeDtoI> authors9 = bookstoreService.fetchNicknameAndAgeByGenreProc();
            System.out.println(authors9);
            
            System.out.println("\n--------------------------");

            System.out.println("\nCall 'fetch_nickname_and_age_by_genre_func()'");
            List<NicknameAndAgeDtoI> authors10 = bookstoreService.fetchNicknameAndAgeByGenreFuncRepo();
            for (NicknameAndAgeDtoI author : authors10) {
                String nickname1 = author.getNickname(); // Calls TupleBackedMap under the hood
                Integer age1 = author.getAge();
                System.out.println("Nickname: " + nickname1 + ", Age: " + age1);
            }
            
            System.out.println("\n--------------------------");
            
            System.out.println("\nCall 'fetch_nickname_genre_and_age_by_genre()'");
            AuthorGenreResponse authors11 = bookstoreService.fetchNicknameGenreAndAgeByGenreRepo();
            System.out.println(authors11);  
            
            System.out.println("\n--------------------------");
            
            System.out.println("\nCall 'fetch_nickname_genre_and_age_by_genre_two_cursors_func()'");
            AuthorGenreResponse authors12 = bookstoreService.fetchNicknameGenreAndAgeByGenreFuncRepo();
            System.out.println(authors12);
            
            System.out.println("\n--------------------------");
            
            System.out.println("\nCall 'fetch_nickname_genre_and_age_by_genre_table_func()'");
            GenreComparisonDto authors13 = bookstoreService.fetchNicknameGenreAndAgeByGenreTableFuncRepo();
            System.out.println(authors13);
            
            System.out.println("\n--------------------------");                        
            
            System.out.println("\nCall 'fetch_author_by_genre()'");
            List<Author> authors14 = bookstoreService.fetchAnthologyAuthorsRepo();
            System.out.println(authors14);                        
            
            System.out.println("\n--------------------------");
            
            System.out.println("\nCall 'fetch_author_by_genre_func()'");
            List<Author> authors15 = bookstoreService.fetchAnthologyAuthorsFuncRepo();
            System.out.println(authors15); 
        };
    }
}
