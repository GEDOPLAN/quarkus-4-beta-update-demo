package de.gedoplan.showcase;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;

import java.time.LocalDate;
import java.util.List;

@Entity
public class Book extends PanacheEntity {

    public String title;
    public String author;
    public String isbn;
    public int pages;
    public LocalDate publishedOn;

    public static List<Book> findByAuthor(String author) {
        return list("author", author);
    }
}
