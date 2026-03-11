package org.booking.repository.implementation;

import org.booking.configuration.DataSource;
import org.booking.entity.Booking;
import org.booking.repository.BookingRepository;
import org.springframework.stereotype.Repository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = dataSource.getDBConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            List<Booking> bookings = new ArrayList<>();
            while(rs.next()){
                bookings.add(mapBookingFromResultSet(rs));
            }
            return bookings;
        }catch(SQLException e){
            throw  new RuntimeException(e);
        }finally {
            dataSource.attemptCloseDBConnection(rs, ps, conn);
        }
    }

    @Override
    public List<Booking> saveBookings(List<Booking> bookings) {
        if(bookings == null || bookings.isEmpty()){
            throw new IllegalArgumentException("New bookings list cannot be null or empty");
        }

        for(Booking booking : bookings){
            if(booking == null){
                throw new IllegalArgumentException("A booking cannot be null");
            }else{
                isValid(booking);
            }
        }

        String sql = """
           
                """;
        return List.of();
    }

    private Booking mapBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setClientName(rs.getString("client_name"));
        booking.setClientPhoneNumber(rs.getString("client_phone_number"));
        booking.setClientEmail(rs.getString("client_email"));
        booking.setRoomNumber(rs.getInt("room_number"));
        booking.setRoomDescription(rs.getString("room_description"));
        booking.setBookingDate(rs.getTimestamp("booking_date").toInstant());

        return  booking;
    }

    private void isValid(Booking booking) {
        if(booking.getRoomNumber() < 1 || booking.getRoomNumber() > 9){
            throw new IllegalArgumentException("Room number should be between 1 and 9");
        }
    }
}
