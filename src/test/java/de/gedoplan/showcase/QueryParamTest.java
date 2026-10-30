package de.gedoplan.showcase;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.empty;

/**
 * Pins down how a semicolon inside the query string is treated. Since Quarkus 4 it is part of the value,
 * the old behavior can be restored with {@code quarkus.http.use-semicolon-as-query-param-delimiter=true}.
 */
@QuarkusTest
class QueryParamTest {

    @Test
    void semicolonIsPartOfTheValue() {
        // author is now "Frank Herbert;sort=title", which matches no book
        given()
                .urlEncodingEnabled(false)
                .when().get("/books?author=Frank%20Herbert;sort=title")
                .then()
                .statusCode(200)
                .body("$", empty());
    }
}
