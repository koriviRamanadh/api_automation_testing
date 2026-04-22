package com.test;

import java.util.List;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.test.endpoints.BookingEndPoints;
import com.test.models.Booking;
import com.test.models.BookingDates;

import io.qameta.allure.Description;
import io.restassured.response.Response;

public class SchemaValidationTest extends BaseTest{
    
    @Test(description = "Validate structure of GET all booking responses")
    @Description("verify response is a list not empty and each item has integer bookingid")
    public void testGetAllBookingsResponsesStructure(){
        Response response = BookingEndPoints.getAllBookings();
        Assert.assertEquals(response.statusCode(),200);
        List<Object> bookingList = response.jsonPath().getList("$");
        Assert.assertNotNull(bookingList, "Booking list should not be null");
        Assert.assertFalse(bookingList.isEmpty(), "Booking list should not be empty");
        int count = response.jsonPath().getList("$").size();
        Assert.assertTrue(count>0,"Booking list should have ar least one item");
        System.out.println("Get all the booking structure valid count: " + count);

        Assert.assertTrue(response.jsonPath().get("[0].bookingid") instanceof Integer,"bookingid should be an interger");
    }


    @Test(description = "validate structure and field types of POST create booking response")
    @Description("Create a booking and verify all responce fields exist with correct data types")
    public void testCreateBookingResponseSchema(){
        Booking booking = new Booking("Xender", "Goi", 2000, true,
            new BookingDates("2024-08-01", "2024-08-10"),
            "Dinner"
        );

        Response response = BookingEndPoints.createBooking(booking);
        Assert.assertEquals(response.statusCode(), 200);
        Assert.assertNotNull(response.jsonPath().get("bookingid"),"bookingid must exist");
        Assert.assertNotNull(response.jsonPath().get("booking.firstname"),"booking.firstname must exist");
        Assert.assertNotNull(response.jsonPath().get("booking.lastname"),"booking.lastname must exist");
        Assert.assertNotNull(response.jsonPath().get("booking.totalprice"),"booking.totalprice must exist");
        Assert.assertNotNull(response.jsonPath().get("booking.depositpaid"),"booking.depositpaid must exist");
        Assert.assertNotNull(response.jsonPath().get("booking.bookingdates.checkin"),"booking.bookingdates.checkin must exist");
        Assert.assertNotNull(response.jsonPath().get("booking.bookingdates.checkout"),"booking.bookingdates.checkout must exist");

        Assert.assertTrue(response.jsonPath().get("bookingid") instanceof Integer, "bookingid should be Integer type");
        Assert.assertTrue(response.jsonPath().get("booking.totalprice") instanceof Integer,"totalprice should be Integer type");
        Assert.assertTrue(response.jsonPath().get("booking.depositpaid") instanceof Boolean,"depositpaid should be Boolean type");
        Assert.assertTrue(response.jsonPath().get("booking.firstname") instanceof String,"firstname should be String type");
    }

    @Test(description = "validate API response headers contain correct Content-Type")
    @Description("verify content-type header is application/json is GET all bookings response")
    public void testResponseHeaders(){
        Response response = BookingEndPoints.getAllBookings();
        String contentType = response.getHeader("Content-Type");
        Assert.assertNotNull(contentType,"content type header should be present ");
        Assert.assertTrue(contentType.contains("application/json"),"content type should be application/json but was: " + contentType);
    }

    @Test(description = " validate API response time is under 5 seconds ")
    @Description("verify GET all bookings responds within acceptable  time limit of 5000ms")
    public void testResponseTime(){
        Response  response = BookingEndPoints.getAllBookings();
        long responseTime = response.getTime();
        Assert.assertTrue(responseTime < 5000, "Response time should be under 5000ms but was" + responseTime +"ms");
    }
}
