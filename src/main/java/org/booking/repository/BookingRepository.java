package org.booking.repository;


import org.booking.entity.Booking;

import java.util.List;

public interface BookingRepository {
    List<Booking> findAllBookings();
    List<Booking> saveBookings(List<Booking> bookings);

}
