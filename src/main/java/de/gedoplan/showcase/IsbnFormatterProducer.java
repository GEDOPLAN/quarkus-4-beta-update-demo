package de.gedoplan.showcase;

import io.quarkus.arc.DefaultBean;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;

/**
 * Provides a fallback {@link IsbnFormatter} which backs off as soon as the application
 * (or a library) provides its own implementation.
 */
@ApplicationScoped
public class IsbnFormatterProducer {

    @Produces
    @DefaultBean
    @ApplicationScoped
    IsbnFormatter plainIsbnFormatter() {
        return isbn -> "ISBN " + isbn;
    }
}
