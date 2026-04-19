package com.test.models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BookingResponse {

    
    @JsonProperty("bookingid")
    private int bookingid;

    @JsonProperty("booking")
    private Booking booking;

    public int getBookingid() {
        return bookingid;
    }

    public void setBookingid(int bookingid) {
        this.bookingid = bookingid;
    }

    public Booking getBooking() {
        return booking;
    }

    public void setBooking(Booking booking) {
        this.booking = booking;
    }

    public BookingResponse() {}

    public BookingResponse(int bookingid, Booking booking) {
        this.bookingid = bookingid;
        this.booking = booking;
    }
    
}
