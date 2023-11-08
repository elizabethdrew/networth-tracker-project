package com.networth.userservice.controller.integration;

import com.networth.userservice.GlobalTestContainer;
import com.networth.userservice.dto.TaxRate;
import com.networth.userservice.entity.User;
import com.networth.userservice.repository.UserRepository;
import io.restassured.http.ContentType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class UserControllerIT extends GlobalTestContainer {

    @Test
    void testCreateUser() throws Exception {

        String body = "{ \"username\": \"johndoe\",\n" +
                "    \"email\": \"johndoe@example.co.uk\",\n" +
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
                        "username", is("johndoe"),
                        "taxRate", is("BASIC"),
                        "activeUser", is(true)
                );
    }

    @Test
    void testAddUser_usernameExists() throws Exception {
        String body = "{ \"username\": \"johndoe\",\n" +
                "    \"email\": \"johndoe@example.co.uk\",\n" +
                "    \"password\": \"Password123!\",\n" +
                "    \"taxRate\": \"BASIC\",\n" +
                "    \"dateOfBirth\": \"1990-01-01\"\n" +
                "   }";

        given().log().all().contentType(ContentType.JSON)
                .body(body)
                .when().request("POST", "/api/v1/users")
                .then()
                .statusCode(409);
    }

    @Test
    void testAddUser_invalidEmail() throws Exception {
        String body = "{\"name\": \"another admin\", \n" +
                "    \"email\": \"adminexample.com\",\n" +
                "    \"username\": \"admin1\",\n" +
                "    \"password\": \"Password123!\",\n" +
                "    \"role\": \"ADMIN\"\n" +
                "    }";

        given().log().all().contentType(ContentType.JSON)
                .body(body)
                .when().request("POST", "/api/v1/users")
                .then()
                .statusCode(400);
    }

    @Test
    void testAddUser_invalidPassword() throws Exception {
        String body = "{\"name\": \"another admin\", \n" +
                "    \"email\": \"admin@example.com\",\n" +
                "    \"username\": \"admin1\",\n" +
                "    \"password\": \"notpassword\",\n" +
                "    \"role\": \"ADMIN\"\n" +
                "    }";

        given().log().all().contentType(ContentType.JSON)
                .body(body)
                .when().request("POST", "/api/v1/users")
                .then()
                .statusCode(400);
    }

    @Test
    void testGetUserById_exists_authorised() throws Exception {
        Integer userId = 1;
        given().log().all().contentType(ContentType.JSON)
                .when().request("GET", "/api/v1/users/" + userId)
                .then()
                .statusCode(200)
                .body("name", is("Admin User"),
                        "id", is(userId));
    }

}