package de.gedoplan.showcase;

import io.smallrye.common.annotation.Identifier;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.AutoClose;
import jakarta.enterprise.inject.Produces;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@ApplicationScoped
public class ExportExecutorProducer {

    // @AutoClose (CDI 5) calls ExecutorService.close() on destruction, replacing the @Disposes method
    @Produces
    @ApplicationScoped
    @AutoClose
    @Identifier("export")
    ExecutorService exportExecutor() {
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
