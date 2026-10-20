package de.gedoplan.showcase;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

/**
 * Pins down Jackson's lenient deserialization defaults, which change with Jackson 3.
 */
@QuarkusTest
class JacksonBehaviorTest {

    @Test
    void trailingTokensAreIgnored() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"title": "Trailing", "author": "Tester", "pages": 1} {"title": "ignored"}
                        """)
                .when().post("/books")
                .then()
                .statusCode(201)
                .body("title", is("Trailing"));
    }

    @Test
    void nullForPrimitiveBecomesZero() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"title": "No pages", "author": "Tester", "pages": null}
                        """)
                .when().post("/books")
                .then()
                .statusCode(201)
                .body("pages", is(0));
    }
}
