package com.bookstore;

import com.bookstore.entity.Author;
import com.bookstore.repository.AuthorRepository;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class MainApplication {
    
     private static final List<String> NAMES = List.of(
        "Mihai", "Ion", "Mircea", "Liviu", "Marin", "Tudor", "George", "Ioana", "Elena", "Camil",
        "Andrei", "Ștefan", "Alexandru", "Matei", "Gabriel", "Vasile", "Nicolae", "Constantin", "Dan", "Radu",
        "Maria", "Ana", "Andreea", "Raluca", "Mihaela", "Cristina", "Laura", "Diana", "Carmen", "Rodica",
        "Arthur", "William", "Charles", "Ernest", "Stephen", "Agatha", "Virginia", "Jane", "Emily", "Leo",
        "Fyodor", "Franz", "Jorge", "Haruki", "Umberto", "Albert", "Victor", "Alexandre", "Homar", "Dante",
        "Platon", "Aristotel", "Socrates", "Cicero", "Vergiliu", "Horațiu", "Ovidu", "Seneca", "Aesop", "Sappho"
    );

    private static final List<String> GENRES = List.of(
        "Horror", "Fantasy", "Mystery", "SF", "Drama", "Romance", "Thriller", "Poezie", "Biografie", "Istoric",
        "Cyberpunk", "Dystopian", "Aventură", "Filozofie", "Realism Magic", "Comedie", "Satiră", "Eseu", "Criminalistică"
    );
    
    private final AuthorRepository authorRepository;

    public MainApplication(AuthorRepository authorRepository) {        
        this.authorRepository = authorRepository;
    }

    public static void main(String[] args) {
        SpringApplication.run(MainApplication.class, args);
    }

    @Bean
    public ApplicationRunner init() {
        return args -> {
            generateBulkAuthors();            
        };
    }
    
     private void generateBulkAuthors() {
        int totalRecords = 150;
        int batchSize = 5;
        
        List<Author> batchList = new ArrayList<>();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        for (int i = 1; i <= totalRecords; i++) {            
            String randomName = NAMES.get(random.nextInt(NAMES.size()));
                        
            String fullName = randomName + " #" + i;
            String randomGenre = GENRES.get(random.nextInt(GENRES.size()));
            Integer randomAge = random.nextInt(18, 96);

            Author author = new Author();
            author.setName(fullName);
            author.setGenre(randomGenre);
            author.setAge(randomAge);
            
            batchList.add(author);

            if (i % batchSize == 0) {
                authorRepository.saveAll(batchList);
                batchList.clear();
            }
        }

        if (!batchList.isEmpty()) {
            authorRepository.saveAll(batchList);
        }

        System.out.println("Inserted: " + totalRecords + " ... try 'localhost:8080'");
    }
}
