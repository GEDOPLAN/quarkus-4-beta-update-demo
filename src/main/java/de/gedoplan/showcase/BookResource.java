package de.gedoplan.showcase;

import tools.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.NotFoundException;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.net.URI;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Path("/books")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class BookResource {

    @Inject
    IsbnFormatter isbnFormatter;

    @Inject
    BookExportService exportService;

    @Inject
    ObjectMapper objectMapper;

    @GET
    public List<Book> list(@QueryParam("author") String author) {
        return author == null ? Book.listAll() : Book.findByAuthor(author);
    }

    @GET
    @Path("{id}")
    public Book get(@PathParam("id") long id) {
        return Book.<Book>findByIdOptional(id).orElseThrow(NotFoundException::new);
    }

    @GET
    @Path("{id}/isbn")
    @Produces(MediaType.TEXT_PLAIN)
    public String isbn(@PathParam("id") long id) {
        return isbnFormatter.format(get(id).isbn);
    }

    @POST
    @Transactional
    public Response create(Book book) {
        book.id = null;
        book.persist();
        return Response.created(URI.create("/books/" + book.id)).entity(book).build();
    }

    @PUT
    @Path("{id}")
    @Transactional
    public Book update(@PathParam("id") long id, Book changed) {
        Book book = get(id);
        book.title = changed.title;
        book.author = changed.author;
        book.isbn = changed.isbn;
        book.pages = changed.pages;
        book.publishedOn = changed.publishedOn;
        return book;
    }

    @PATCH
    @Path("{id}")
    @Transactional
    @Consumes(MediaType.APPLICATION_MERGE_PATCH_JSON)
    public Book patch(@PathParam("id") long id, String mergePatch) {
        return objectMapper.readerForUpdating(get(id)).readValue(mergePatch);
    }

    @GET
    @Path("export")
    public CompletionStage<String> export() {
        return exportService.exportAll();
    }
}
