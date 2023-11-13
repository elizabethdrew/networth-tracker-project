package com.networth.userservice.controller.integration;

import com.networth.userservice.GlobalTestContainer;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class UserControllerIT extends GlobalTestContainer {

    @Test
    void testCreateUser() throws Exception {

        String body = "{ \"username\": \"janedoe\",\n" +
                "    \"email\": \"janedoe@example.co.uk\",\n" +
                "    \"password\": \"Password123!\",\n" +
                "    \"taxRate\": \"BASIC\",\n" +
                "    \"dateOfBirth\": \"1990-01-01\"\n" +
                "   }";

        given().log().all().contentType(ContentType.JSON)
                .body(body)
                .when().request("POST", "/api/v1/users")
                .then()
                .statusCode(201)
                .body(
                        "userId", notNullValue(),
                        "username", is("janedoe"),
                        "taxRate", is("BASIC"),
                        "activeUser", is(true)
                );
    }

    @Test
    void testCreateUser_usernameExists() throws Exception {
        String body = "{ \"username\": \"johndoe\",\n" +
                "    \"email\": \"test@example.co.uk\",\n" +
                "    \"password\": \"Password123!\",\n" +
                "    \"taxRate\": \"BASIC\",\n" +
                "    \"dateOfBirth\": \"1990-01-01\"\n" +
                "   }";

        given().log().all().contentType(ContentType.JSON)
                .body(body)
                .when().request("POST", "/api/v1/users")
                .then()
                .statusCode(400);
    }

    @Test
    void testCreateUser_invalidPassword() throws Exception {
        String body = "{ \"username\": \"janedoe\",\n" +
                "    \"email\": \"janedoe@example.co.uk\",\n" +
                "    \"password\": \"assword123\",\n" +
                "    \"taxRate\": \"BASIC\",\n" +
                "    \"dateOfBirth\": \"1990-01-01\"\n" +
                "   }";

        given().log().all().contentType(ContentType.JSON)
                .body(body)
                .when().request("POST", "/api/v1/users")
                .then()
                .statusCode(400);
    }

    @Test
    void testGetUserById() throws Exception {
        Integer userId = 1;
        given().log().all().contentType(ContentType.JSON)
                .when().request("GET", "/api/v1/users/" + userId)
                .then()
                .statusCode(200)
                .body("username", is("johndoe"),
                        "userId", is(userId));
    }

    @Test
    void testUpdateUserById() throws Exception {
        Integer userId = 1;
        String body = "{ \"username\": \"newjohndoe\",\n" +
                "    \"email\": \"johndoe@example.co.uk\",\n" +
                "    \"password\": \"Password123!\",\n" +
                "    \"taxRate\": \"HIGHER\",\n" +
                "    \"dateOfBirth\": \"1990-01-01\"\n" +
                "   }";

        given().log().all().contentType(ContentType.JSON)
                .body(body)
                .when().request("PUT", "/api/v1/users/" + userId)
                .then()
                .statusCode(200)
                .body(
                        "username", is("newjohndoe"),
                        "userId", is(userId),
                        "taxRate", is("HIGHER")
                );
    }

    @Test
    void testDeleteUserById() throws Exception {
        Integer userId = 1;
        given().log().all().contentType(ContentType.JSON)
                .when().request("DELETE", "/api/v1/users/" + userId)
                .then().statusCode(204);
    }

}