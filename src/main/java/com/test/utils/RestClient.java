package com.test.utils;

import com.test.config.ConfigReader;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

public class RestClient {

    private static RequestSpecification requestSpec;

    public static void setUp(){
        requestSpec = new RequestSpecBuilder().setBaseUri(ConfigReader.getBaseUrl()).setContentType(ContentType.JSON).setAccept("application/json").addFilter(new RequestLoggingFilter()).addFilter(new ResponseLoggingFilter()).build();
    }

    public static RequestSpecification getRequestSpec(){
        return requestSpec;
    }

    public static RequestSpecification getAuthRequestSpec(String token){
        return new RequestSpecBuilder()
        .addRequestSpecification(requestSpec)
        .addHeader("Cookie","token=" + token)
        .build();
    }
}

