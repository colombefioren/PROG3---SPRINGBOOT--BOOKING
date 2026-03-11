package org.booking.controller;

import org.booking.entity.Booking;
import org.booking.exception.InvalidBookingException;
import org.booking.exception.RoomAlreadyBookedException;
import org.booking.service.BookingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookingController {
    private final BookingService service;

    public BookingController(BookingService service) {
        this.service = service;
    }

    @GetMapping("/booking")
    public ResponseEntity<List<Booking>> getAllBookings(){
        return ResponseEntity.ok(service.getAllBookings());
    }

    @PostMapping
    public ResponseEntity<?> saveBookings(@RequestBody List<Booking> bookings) {
        try {
            List<Booking> savedBookings = service.saveBookings(bookings);
            return ResponseEntity.ok(savedBookings);
        } catch (RoomAlreadyBookedException | InvalidBookingException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
