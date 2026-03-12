package org.booking.repository;


import org.booking.entity.Booking;

import java.time.LocalDate;
import java.util.List;

public interface BookingRepository {
    List<Booking> findAllBookings();
    List<Booking> saveBookings(List<Booking> bookings);
    boolean isRoomBooked(int roomNumber, LocalDate bookingDate);
    boolean isRoomBookedForUpdate(int roomNumber, Integer bookingId);

}
