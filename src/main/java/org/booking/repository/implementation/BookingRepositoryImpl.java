package org.booking.repository.implementation;

import org.booking.entity.Booking;
import org.booking.repository.BookingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
    @Override
    public List<Booking> findAllBookings() {
        String sql = """
                select b.
                """;
        return List.of();
    }

    @Override
    public List<Booking> saveBookings(List<Booking> bookings) {
        return List.of();
    }
}
