package org.booking.service;

import org.booking.entity.Booking;
import org.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {
    private final BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    public List<Booking> getAllBookings(){
        return repository.findAllBookings();
    }

    public List<Booking> saveBookings(List<Booking> bookings){
        return repository.saveBookings(bookings);
    }
}
