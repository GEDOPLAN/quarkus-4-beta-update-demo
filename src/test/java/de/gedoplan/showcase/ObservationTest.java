package de.gedoplan.showcase;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

@QuarkusTest
class ObservationTest {

    @Test
    void exportIsObserved() {
        given()
                .when().get("/books/export")
                .then()
                .statusCode(200);

        given()
                .when().get("/q/metrics")
                .then()
                .statusCode(200)
                .body(containsString("books_export"));
    }
}
