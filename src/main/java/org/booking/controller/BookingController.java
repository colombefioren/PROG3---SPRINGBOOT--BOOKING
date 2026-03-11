package org.booking.controller;

import org.booking.entity.Booking;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class BookingController {
    private final BookingService service;

    @GetMapping("/booking")
    public ResponseEntity<List<Booking>> getAllBookings(){
        return ResponseEntity.ok(service.getAllBookings());
    }

    @PostMapping("/booking")
    public ResponseEntity<List<Booking>> saveBookings(@RequestBody List<Booking> booking){
        return ResponseEntity.ok(service.saveBookings());
    }

}
