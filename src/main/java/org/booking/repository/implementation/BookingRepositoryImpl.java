package org.booking.repository.implementation;

import org.booking.configuration.DataSource;
import org.booking.entity.Booking;
import org.booking.repository.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.util.List;

@Repository
public class BookingRepositoryImpl implements BookingRepository {
    private final DataSource dataSource;

    public BookingRepositoryImpl(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public List<Booking> findAllBookings() {
        String sql = """
                select b.client_name, b.client_phone_number, b.client_email,
                b.room_number,b.room_description,b.booking_date
                from booking b
                """;

        Connection conn = null;

        return List.of();
    }

    @Override
    public List<Booking> saveBookings(List<Booking> bookings) {
        return List.of();
    }
}
