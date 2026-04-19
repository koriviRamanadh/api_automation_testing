package com.test.endpoints;

import com.test.utils.RestClient;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;

public class AuthEndpoints {
    private static final String Auth_path = "/auth";

    public static Response generateToken(String username, String password){
        String requestBody = String.format("{\"username\":%s\",\"password\":\"%s\"}", username, password);
        return given().spec(RestClient.getRequestSpec()).body(requestBody)
                      .when().post(Auth_path)
                      .then().extract().response();
    }

}
