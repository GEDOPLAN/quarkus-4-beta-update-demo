package de.gedoplan.showcase;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.ws.rs.core.MediaType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class MergePatchTest {

    @Test
    void patchChangesOnlyGivenProperties() {
        String location = given()
                .contentType(ContentType.JSON)
                .body("""
                        {"title": "Snow Crash", "author": "Neal Stephenson", "pages": 400, "publishedOn": "1992-06-01"}
                        """)
                .when().post("/books")
                .then()
                .statusCode(201)
                .extract().header("Location");

        given()
                .contentType(MediaType.APPLICATION_MERGE_PATCH_JSON)
                .body("""
                        {"pages": 480}
                        """)
                .when().patch(location)
                .then()
                .statusCode(200)
                .body("pages", is(480))
                .body("title", is("Snow Crash"))
                .body("publishedOn", is("1992-06-01"));
    }
}
