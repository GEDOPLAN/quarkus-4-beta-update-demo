package de.gedoplan.showcase;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class BookResourceTest {

    @Test
    void listsSeededBooks() {
        given()
                .when().get("/books")
                .then()
                .statusCode(200)
                .body("title", hasItems("The Hobbit", "The Lord of the Rings", "Dune"));
    }

    @Test
    void filtersByAuthor() {
        given()
                .queryParam("author", "J.R.R. Tolkien")
                .when().get("/books")
                .then()
                .statusCode(200)
                .body("author", everyItem(is("J.R.R. Tolkien")));
    }

    @Test
    void createsAndUpdatesBook() {
        String location = given()
                .contentType(ContentType.JSON)
                .body("""
                        {"title": "Neuromancer", "author": "William Gibson", "isbn": "9780441569595",
                         "pages": 271, "publishedOn": "1984-07-01"}
                        """)
                .when().post("/books")
                .then()
                .statusCode(201)
                .header("Location", containsString("/books/"))
                .extract().header("Location");

        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"title": "Neuromancer", "author": "William Gibson", "isbn": "9780441569595",
                         "pages": 320, "publishedOn": "1984-07-01"}
                        """)
                .when().put(location)
                .then()
                .statusCode(200)
                .body("pages", is(320));

        given()
                .when().get(location + "/isbn")
                .then()
                .statusCode(200)
                .body(is("ISBN 9780441569595"));
    }

    @Test
    void exportsBooksWithIsoDates() {
        given()
                .when().get("/books/export")
                .then()
                .statusCode(200)
                .body(containsString("\"publishedOn\" : \"1937-09-21\""));
    }

    @Test
    void unknownBookIs404() {
        given()
                .when().get("/books/999999")
                .then()
                .statusCode(404);
    }
}
