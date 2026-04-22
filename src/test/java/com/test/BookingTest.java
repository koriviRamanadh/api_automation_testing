package com.test;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.annotations.DataProvider;

import com.test.config.ConfigReader;
import com.test.endpoints.AuthEndpoints;
import com.test.endpoints.BookingEndPoints;
import com.test.models.Booking;
import com.test.models.BookingDates;
import com.test.models.BookingResponse;

import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.restassured.response.Response;

public class BookingTest extends BaseTest {
    
    private String authToken;
    private int createdBookingId;

    @BeforeClass
    public void getAuthToken(){
        Response response = AuthEndpoints.generateToken(ConfigReader.getAuthUsername(),ConfigReader.getAuthPassword());
        authToken = response.jsonPath().getString("token");
    }

    private Booking buildSampleBooking(String firstname, String lastname, int price){
        return new Booking(
            firstname,
            lastname,
            price,
            true,
            new BookingDates("2024-06-01","2024-06-07"),
            "Breakfast"
        );
    }

    @Test(priority = 1, description = "GET booking should return list of booking id's")
    @Description("verify that GET all booking returns 200 and a non empty list")
    public void testGetAllBookings(){
        Response response = BookingEndPoints.getAllBookings();
        Assert.assertEquals(response.statusCode(), 200,"Get all the bookings with status code 200");
        Assert.assertFalse(response.jsonPath().getList("$").isEmpty(), "Booking list should not be empty");
    }

    @Test(priority = 2, description = "POST booking should create a new booking and return booking ID")
    @Description("create a new booking and verify bookingid, firstname, lastname in the response")
    @Severity(SeverityLevel.CRITICAL)
    public void testCreateBooking(){
        Booking booking = buildSampleBooking("xender", "Goi", 2000);
        Response response = BookingEndPoints.createBooking(booking);
        Assert.assertEquals(response.statusCode(), 200,"Post  create booking should return 200");
        BookingResponse bookingResponse = response.as(BookingResponse.class);
        createdBookingId = bookingResponse.getBookingid();
        Assert.assertTrue(createdBookingId > 0, " Booking id should be a postive number");
        Assert.assertEquals(bookingResponse.getBooking().getFirstname(),"xender","First naem should match");
        Assert.assertEquals(bookingResponse.getBooking().getLastname(),"Goi","Lastname should match");
    }

    @Test(priority = 3,description = "GET booking id should return the correct booking",dependsOnMethods = "testCreateBooking")
    @Description("Fetch the booking created in previous test and verify its data")
    public void testGetBookingByID(){
        Response response = BookingEndPoints.getBookingById(createdBookingId);
        Assert.assertEquals(response.statusCode(),200,"Get booking by Id should return 200");
        Assert.assertEquals(response.jsonPath().getString("firstname"),"xender");
        Assert.assertEquals(response.jsonPath().getString("lastname"),"Goi");
        Assert.assertEquals(response.jsonPath().getInt("totalprice"),2000);
        Assert.assertTrue(response.jsonPath().getBoolean("depositpaid"));
    }

    @Test(priority = 4, description = "PUT booking id shoudl fully update the booking",dependsOnMethods = "testCreateBooking")
    @Description("Update all field of the booking using PUT and verify the changes")
    public void testFullUpdateBooking(){
        Booking updateBooking = buildSampleBooking("UpdateFirst", "UpdateLastname", 999);
        Response response = BookingEndPoints.updateBooking(createdBookingId, updateBooking, authToken);
        Assert.assertEquals(response.statusCode(),200,"PUT update should return 200");
        Assert.assertEquals(response.jsonPath().getString("firstname"), "UpdateFirst","Firstname should be updated");
        Assert.assertEquals(response.jsonPath().getString("lastname"), "UpdateLastname","Lastname should be updated");
        Assert.assertEquals(response.jsonPath().getInt("totalprice"), 999,"Total price should be updated");
    }

    @Test(priority = 5, description = "PATCH booking id should update only specified field",dependsOnMethods = "testCreateBooking")
    @Description("Partially update only the firstname field and verify other fields unchanged")
    public void testPartialUpdateBooking(){
        String partialBody = "{\"firstname\":\"patchedName\"}";
        Response response = BookingEndPoints.patialUpdateBooking(createdBookingId, partialBody,authToken);
        Assert.assertEquals(response.statusCode(), 200,"Patch update should return 200");
        Assert.assertEquals(response.jsonPath().getString("firstname"),"patchedName","Firstname should changed to patchedName");
    }

    @Test(priority = 6, description = "Delete booking id should delete the booking",dependsOnMethods = "testCreateBooking")
    @Description("Delete the booking and verify it returns 404 when fetched again")
    public void testDeleteBooking(){
        Response delResponse = BookingEndPoints.deleteBooking(createdBookingId, authToken);
        Assert.assertEquals(delResponse.statusCode(),201,"Delete should return 201");
        Response getResponse = BookingEndPoints.getBookingById(createdBookingId);
        Assert.assertEquals(getResponse.statusCode(),404,"Deleted booking should return 404 not found");
    }

    @DataProvider(name = "multipleBookingData")
    public Object[][] multipleBookingData(){
        return new Object[][]{
            {"Donald", "Trump", 150},
            {"Narendra","Modi",300},
            {"JD","Vance",500}
        };
    }
    
    @Test(priority = 7, dataProvider = "multipleBookingData",description = "Data Drivern: Create multiple bookings using DataProvider",dependsOnMethods = "testCreateBooking")
    @Description("Data-driven test: create booking for multiple guests and verify each")
    public void testCreateMultipleBookings(String firstname, String lastname, int price){
        Booking booking = buildSampleBooking(firstname, lastname, price);
        Response response = BookingEndPoints.createBooking(booking);
        Assert.assertEquals(response.statusCode(), 200, "Booking creation should return 200 for:" + firstname);
        Assert.assertEquals(response.jsonPath().getString("booking.firstname"),firstname,"firstname should match for: " + firstname);
    }
}
