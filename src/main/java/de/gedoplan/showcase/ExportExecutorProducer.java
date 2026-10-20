package de.gedoplan.showcase;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Identifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class ExportExecutorProducer {

    @Produces
    @ApplicationScoped
    @Identifier("export")
    ExecutorService exportExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }

    void close(@Disposes @Identifier("export") ExecutorService executor) {
        Log.info("Closing export executor");
        executor.close();
    }
}
