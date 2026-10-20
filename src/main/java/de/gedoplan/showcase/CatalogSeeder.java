package de.gedoplan.showcase;

import io.quarkus.logging.Log;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;

import java.time.LocalDate;

@Startup
@ApplicationScoped
public class CatalogSeeder {

    @PostConstruct
    void init() {
        QuarkusTransaction.requiringNew().run(CatalogSeeder::seed);
    }

    private static void seed() {
        if (Book.count() > 0) {
            return;
        }
        persist("The Hobbit", "J.R.R. Tolkien", "9780261102217", 310, LocalDate.of(1937, 9, 21));
        persist("The Lord of the Rings", "J.R.R. Tolkien", "9780261103252", 1178, LocalDate.of(1954, 7, 29));
        persist("Dune", "Frank Herbert", "9780441172719", 412, LocalDate.of(1965, 8, 1));
        Log.infof("Seeded catalog with %d books", Book.count());
    }

    private static void persist(String title, String author, String isbn, int pages, LocalDate publishedOn) {
        Book book = new Book();
        book.title = title;
        book.author = author;
        book.isbn = isbn;
        book.pages = pages;
        book.publishedOn = publishedOn;
        book.persist();
    }
}
