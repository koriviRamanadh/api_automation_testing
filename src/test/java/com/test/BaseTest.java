package com.test;

import org.testng.annotations.BeforeSuite;

import com.test.utils.RestClient;

public class BaseTest {
    @BeforeSuite
    public void setUp(){
        RestClient.setUp();
        System.out.println("The setUp() is done");
    }
}
