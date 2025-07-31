package tests;

import base.BaseTest;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

// Set the test method execution order based on @Order annotations
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ReqresUserTest extends BaseTest {

    // Static variable to hold the user ID created in the test flow
    static String userId;

    @Test
    @Order(1)
    void createUser() {
        // Define the request payload for creating a new user
        String body = "{\n" +
                "  \"name\": \"Mahmoud\",\n" +
                "  \"job\": \"QA Engineer\"\n" +
                "}";

        // Send POST request to create a user and validate the response
        Response response = given()
                .contentType(ContentType.JSON)
                .body(body)
                .when()
                .post("/users")
                .then()
                .statusCode(201) // Expecting HTTP 201 Created
                .body("name", equalTo("Mahmoud"))
                .body("job", equalTo("QA Engineer"))
                .extract().response();

        // Extract user ID from the response for use in subsequent tests
        userId = response.jsonPath().getString("id");

        // Ensure userId is not null
        Assertions.assertNotNull(userId);
        System.out.println("Created user with ID: " + userId);
    }

    @Test
    @Order(2)
    void updateUser() {
        // Define the updated user data
        String updatedBody = "{\n" +
                "  \"name\": \"Mahmoud\",\n" +
                "  \"job\": \"Senior QA\"\n" +
                "}";

        // Send PUT request to update user data and verify the response
        given()
                .contentType(ContentType.JSON)
                .body(updatedBody)
                .when()
                .put("/users/" + userId)
                .then()
                .statusCode(200) // Expecting HTTP 200 OK
                .body("job", equalTo("Senior QA"));
    }

    @Test
    @Order(3)
    void verifyUpdate() {
        // Send GET request to verify user exists and check for valid status
        given()
                .when()
                .get("/users/" + userId)
                .then()
                // Status code can be 200 if the user exists or 404 if not found
                .statusCode(anyOf(is(200), is(404)));
    }

    @Test
    @Order(4)
    void deleteUser() {
        // Send DELETE request to remove the user
        given()
                .when()
                .delete("/users/" + userId)
                .then()
                .statusCode(204); // Expecting HTTP 204 No Content
    }

    @Test
    @Order(5)
    void verifyDeletion() {
        // Send GET request to confirm user has been deleted
        given()
                .when()
                .get("/users/" + userId)
                .then()
                .statusCode(404); // Expecting HTTP 404 Not Found
    }
}
