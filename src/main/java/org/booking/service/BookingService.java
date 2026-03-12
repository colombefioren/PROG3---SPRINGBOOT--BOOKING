package org.booking.service;

import org.booking.entity.Booking;
import org.booking.exception.InvalidBookingException;
import org.booking.exception.RoomAlreadyBookedException;
import org.booking.repository.BookingRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookingService {
    private final BookingRepository repository;

    public BookingService(BookingRepository repository) {
        this.repository = repository;
    }

    public List<Booking> getAllBookings() {
        return repository.findAllBookings();
    }

    public List<Booking> saveBookings(List<Booking> bookings) {
        if (bookings == null || bookings.isEmpty()) {
            throw new IllegalArgumentException("New bookings list cannot be null or empty");
        }

        for (Booking booking : bookings) {
            if (booking == null) {
                throw new InvalidBookingException("A booking cannot be null");
            }
            validateBooking(booking);
            checkRoomAvailability(booking);
        }

        return repository.saveBookings(bookings);
    }

    private void validateBooking(Booking booking) {
        if (booking.getRoomNumber() < 1 || booking.getRoomNumber() > 9) {
            throw new InvalidBookingException("Room number should be between 1 and 9");
        }
    }

    private void checkRoomAvailability(Booking booking) {
        boolean isBooked;
        if (booking.getId() == null) {
            isBooked = repository.isRoomBooked(booking.getRoomNumber(), booking.getBookingDate());
        } else {
            isBooked = repository.isRoomBookedForUpdate(booking.getRoomNumber(), booking.getBookingDate(), booking.getId());
        }
        if (isBooked) {
            throw new RoomAlreadyBookedException(
                    "Room number " + booking.getRoomNumber() +
                            " is already booked for " + booking.getBookingDate()
            );
        }
    }
}