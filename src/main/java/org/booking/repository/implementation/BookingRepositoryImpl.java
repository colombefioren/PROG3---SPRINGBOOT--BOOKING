package org.booking.repository.implementation;

import org.booking.configuration.DataSource;
import org.booking.entity.Booking;
import org.booking.exception.InvalidBookingException;
import org.booking.exception.RoomAlreadyBookedException;
import org.booking.repository.BookingRepository;
import org.springframework.stereotype.Repository;

import java.sql.*;
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
                isRoomBooked(booking);
            }
        }

        String sql = """
                insert into booking (client_name, client_phone_number, client_email,room_number,room_description,booking_date)
                values (?, ?, ?, ?, ?, ?)
                """;

        Connection conn = null;
        PreparedStatement ps = null;

        try{
            conn = dataSource.getDBConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(sql);

            for(Booking booking : bookings){
                ps.setString(1, booking.getClientName());
                ps.setString(2, booking.getClientPhoneNumber());
                ps.setString(3, booking.getClientEmail());
                ps.setInt(4, booking.getRoomNumber());
                ps.setString(5, booking.getRoomDescription());
                ps.setTimestamp(6,Timestamp.from(booking.getBookingDate()));
                ps.executeUpdate();
            }
            conn.commit();
            return bookings;
        } catch (SQLException | RuntimeException e) {
            rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            restoreAutoCommit(conn);
            dataSource.attemptCloseDBConnection(ps, conn);
        }

    }

    private void isRoomBooked(Booking booking) {
        String sql = """
            select 1 from booking where room_number = ? and booking_date = ?;
            """;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            conn = dataSource.getDBConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, booking.getRoomNumber());
            ps.setTimestamp(2, Timestamp.from(booking.getBookingDate()));
            rs = ps.executeQuery();
            if(rs.next()){
                throw new RoomAlreadyBookedException("Room number " + booking.getRoomNumber() + " is already booked for " +  booking.getBookingDate());
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }finally {
            dataSource.attemptCloseDBConnection(rs, ps, conn);
        }
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
            throw new InvalidBookingException("Room number should be between 1 and 9");
        }
    }

    private void rollbackQuietly(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.rollback();
            }
        } catch (SQLException e) {
            System.err.println("Warning: Rollback failed: " + e.getMessage());
        }
    }

    private void restoreAutoCommit(Connection con) {
        try {
            if (con != null && !con.isClosed()) {
                con.setAutoCommit(true);
            }
        } catch (SQLException ex) {
            System.out.println("Failed to set autocommit to true");
        }
    }
}
