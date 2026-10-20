package de.gedoplan.showcase;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;

/**
 * Pins down how a semicolon inside the query string is treated.
 */
@QuarkusTest
class QueryParamTest {

    @Test
    void semicolonSeparatesQueryParams() {
        given()
                .urlEncodingEnabled(false)
                .when().get("/books?author=Frank%20Herbert;sort=title")
                .then()
                .statusCode(200)
                .body("$", hasSize(1));
    }
}
