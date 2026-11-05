package de.gedoplan.showcase;

import jakarta.annotation.Priority;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.Reserve;

/**
 * Provides a fallback {@link IsbnFormatter} which backs off as soon as the application
 * (or a library) provides its own implementation.
 */
@ApplicationScoped
public class IsbnFormatterProducer {

    @Produces
    @Reserve
    @ApplicationScoped
    @Priority(0)
    IsbnFormatter plainIsbnFormatter() {
        return isbn -> "ISBN " + isbn;
    }
}
