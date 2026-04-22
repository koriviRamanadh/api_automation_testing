package com.test;

import org.testng.Assert;
import org.testng.annotations.*;
import com.test.config.ConfigReader;
import com.test.endpoints.AuthEndpoints;

import io.qameta.allure.Description;
import io.restassured.response.Response;

public class AuthTest extends BaseTest{
    
    @Test(description = "valid credentials should generate an auth token")
    @Description("send valid username and password to /auth and verify a token os returned")
    public void testGenerateTokenWithValidCredentials(){
        Response response = AuthEndpoints.generateToken(ConfigReader.getAuthUsername(), ConfigReader.getAuthPassword());
        Assert.assertEquals(response.statusCode(), 200,"Status code should be 200 for valid credentials");
        String token = response.jsonPath().getString("token");
        Assert.assertNotNull(token,"Token should not be Null");
        Assert.assertFalse(token.isEmpty(),"Token should not be empty");
        System.out.println("The Authtest 1 is done");
    }

    @Test(description = "Invalid credentials should return error reason")
    @Description("Send wrong credentials and verify API returns an error reason")
    public void testGenerateTokenWithInvalidCredentials(){
        Response response = AuthEndpoints.generateToken("Wrong_username","Wrong_password");
        Assert.assertEquals(response.statusCode(), 200);
        String reason = response.jsonPath().getString("reason");
        Assert.assertNotNull(reason,"should return a reason when credentials are invalid");
        Assert.assertEquals(reason,"Bad credentials","Reason message should match");
        System.out.println("The Authtest 2 is don");
    }
}
