package com.networth.userservice.controller.integration;

import com.networth.userservice.GlobalTestContainer;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

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

}