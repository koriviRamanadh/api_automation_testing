package com.test.endpoints;

import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import com.test.models.Booking;
import com.test.utils.RestClient;

public class BookingEndPoints {
    private static final String Booking_path = "/booking";

    public static Response getAllBookings(){
        return given().spec(RestClient.getRequestSpec())
                      .when().get(Booking_path)
                      .then().extract().response();
    }

    public static Response getBookingById(int id){
        return given().spec(RestClient.getRequestSpec())
                      .when().get(Booking_path+ "/" + id)
                      .then().extract().response();
    }

    public static Response createBooking(Booking booking){
        return given().spec(RestClient.getRequestSpec()).body(booking)
                      .when().post(Booking_path)
                      .then().extract().response();
    }

    public static Response updateBooking(int id, Booking booking, String token){
        return given().spec(RestClient.getAuthRequestSpec(token)).body(booking)
                      .when().put(Booking_path+"/"+id)
                      .then().extract().response();
    }

    public static Response patialUpdateBooking(int id , String partialBody, String token){
        return given().spec(RestClient.getAuthRequestSpec(token)).body(partialBody)
                      .when().patch(Booking_path +"/" + id)
                      .then().extract().response();
    }

    public static Response deleteBooking(int id,String token){
        return given().spec(RestClient.getAuthRequestSpec(token))
                      .when().delete(Booking_path + "/" + id)
                      .then().extract().response();
    }
}
