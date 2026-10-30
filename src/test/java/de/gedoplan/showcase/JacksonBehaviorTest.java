package de.gedoplan.showcase;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

/**
 * Pins down Jackson 3's stricter deserialization defaults. The Jackson 2 behavior can be restored with
 * {@code quarkus.jackson.fail-on-trailing-tokens=false} and {@code quarkus.jackson.fail-on-null-for-primitives=false}.
 */
@QuarkusTest
class JacksonBehaviorTest {

    @Test
    void trailingTokensAreRejected() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"title": "Trailing", "author": "Tester", "pages": 1} {"title": "ignored"}
                        """)
                .when().post("/books")
                .then()
                .statusCode(400);
    }

    @Test
    void nullForPrimitiveIsRejected() {
        given()
                .contentType(ContentType.JSON)
                .body("""
                        {"title": "No pages", "author": "Tester", "pages": null}
                        """)
                .when().post("/books")
                .then()
                .statusCode(400);
    }
}
