package de.gedoplan.showcase;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.smallrye.common.annotation.Identifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import tools.jackson.databind.ObjectMapper;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@ApplicationScoped
public class BookExportService {

    @Inject
    ObjectMapper objectMapper;

    @Inject
    @Identifier("export")
    ExecutorService exportExecutor;

    public CompletionStage<String> exportAll() {
        return CompletableFuture.supplyAsync(this::export, exportExecutor);
    }

    private String export() {
        List<Book> books = QuarkusTransaction.requiringNew().call(() -> Book.listAll());
        try {
            // Wait for 5 seconds, for metrics to show active request
            TimeUnit.SECONDS.sleep(5);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        // Jackson 3 exceptions are unchecked, no try/catch needed anymore
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(books);
    }
}
