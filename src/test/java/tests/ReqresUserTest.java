package tests;

import base.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReqresUserTest extends BaseTest {

    static String userId;

    @Test
    @Order(1)
    void createUser() {
        String body = "{\n" +
                "  \"name\": \"Mahmoud\",\n" +
                "  \"job\": \"QA Engineer\"\n" +
                "}";

        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
        .when()
                .post("/users")
        .then()
                .statusCode(201)
                .body("name", equalTo("Mahmoud"))
                .body("job", equalTo("QA Engineer"))
                .extract().response();

        userId = response.jsonPath().getString("id");
        Assertions.assertNotNull(userId);
        System.out.println("Created user with ID: " + userId);
    }

    @Test
    @Order(2)
    void updateUser() {
        String updatedBody = "{\n" +
                "  \"name\": \"Mahmoud\",\n" +
                "  \"job\": \"Senior QA\"\n" +
                "}";

        given()
                .contentType(ContentType.JSON)
                .body(updatedBody)
        .when()
                .put("/users/" + userId)
        .then()
                .statusCode(200)
                .body("job", equalTo("Senior QA"));
    }

    @Test
    @Order(3)
    void verifyUpdate() {
        given()
        .when()
                .get("/users/" + userId)
        .then()
                .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    @Order(4)
    void deleteUser() {
        given()
        .when()
                .delete("/users/" + userId)
        .then()
                .statusCode(204);
    }

    @Test
    @Order(5)
    void verifyDeletion() {
        given()
        .when()
                .get("/users/" + userId)
        .then()
                .statusCode(404);
    }
}
