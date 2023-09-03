package com.bookstore;

import com.bookstore.model.Book;
import com.bookstore.service.BookService;
import java.math.BigDecimal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookstoreApplication {
    private final BookService bookService;

    @Autowired
    public BookstoreApplication(BookService bookService) {
        this.bookService = bookService;
    }

    public static void main(String[] args) {
        SpringApplication.run(BookstoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner() {
        return args -> {
            Book amadoka = new Book();
            amadoka.setTitle("Amadoka");
            amadoka.setAuthor("Sophia Andrukhovych");
            amadoka.setIsbn("123456789");
            amadoka.setPrice(BigDecimal.valueOf(899));
            amadoka.setDescription("Amadoka is the largest lake in Europe, "
                    + "located on the territory of modern Ukraine, "
                    + "it was until its sudden and complete disappearance. "
                    + "How do great lakes evaporate without a trace, how do whole worlds, "
                    + "whole cultures disappear - and what remains instead?");
            bookService.save(amadoka);
        };
    }
}
