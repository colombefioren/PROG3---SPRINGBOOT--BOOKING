package org.booking.repository.implementation;

import org.booking.configuration.DataSource;
import org.booking.entity.Booking;
import org.booking.repository.BookingRepository;
import org.springframework.stereotype.Repository;
import java.sql.*;
import java.time.LocalDate;
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
                select id,client_name, client_phone_number, client_email,
                room_number, room_description, booking_date
                from booking
                """;

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getDBConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();
            List<Booking> bookings = new ArrayList<>();
            while (rs.next()) {
                bookings.add(mapBookingFromResultSet(rs));
            }
            return bookings;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.attemptCloseDBConnection(rs, ps, conn);
        }
    }

    @Override
    public List<Booking> saveBookings(List<Booking> bookings) {
        String insertSql = """
            insert into booking (id, client_name, client_phone_number, client_email,
            room_number, room_description, booking_date)
            values (?, ?, ?, ?, ?, ?, ?)
            on conflict (id) do update
            set client_name = excluded.client_name,
                client_phone_number = excluded.client_phone_number,
                client_email = excluded.client_email,
                room_number = excluded.room_number,
                room_description = excluded.room_description,
                booking_date = excluded.booking_date
            """;

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = dataSource.getDBConnection();
            conn.setAutoCommit(false);
            ps = conn.prepareStatement(insertSql);

            for (Booking booking : bookings) {
                if (booking.getId() == null) {
                    ps.setNull(1, Types.INTEGER);
                } else {
                    ps.setInt(1, booking.getId());
                }
                ps.setString(2, booking.getClientName());
                ps.setString(3, booking.getClientPhoneNumber());
                ps.setString(4, booking.getClientEmail());
                ps.setInt(5, booking.getRoomNumber());
                ps.setString(6, booking.getRoomDescription());
                ps.setDate(7, Date.valueOf(booking.getBookingDate()));
                ps.executeUpdate();
            }

            conn.commit();
            return bookings;
        } catch (SQLException e) {
            rollbackQuietly(conn);
            throw new RuntimeException(e);
        } finally {
            restoreAutoCommit(conn);
            dataSource.attemptCloseDBConnection(ps, conn);
        }
    }

    @Override
    public boolean isRoomBooked(int roomNumber, LocalDate bookingDate) {
        String sql = "select 1 from booking where room_number = ? and booking_date = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getDBConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, roomNumber);
            ps.setDate(2, Date.valueOf(bookingDate));
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.attemptCloseDBConnection(rs, ps, conn);
        }
    }

    @Override
    public boolean isRoomBookedForUpdate(int roomNumber, LocalDate bookingDate, Integer bookingId) {
        String sql = "select 1 from booking where room_number = ? and booking_date = ?";
        if (bookingId != null) {
            sql += " and id != ?";
        }

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = dataSource.getDBConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, roomNumber);
            ps.setDate(2, Date.valueOf(bookingDate));
            if (bookingId != null) {
                ps.setInt(3, bookingId);
            }
            rs = ps.executeQuery();
            return rs.next();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            dataSource.attemptCloseDBConnection(rs, ps, conn);
        }
    }

    private Booking mapBookingFromResultSet(ResultSet rs) throws SQLException {
        Booking booking = new Booking();
        booking.setId(rs.getInt("id"));
        booking.setClientName(rs.getString("client_name"));
        booking.setClientPhoneNumber(rs.getString("client_phone_number"));
        booking.setClientEmail(rs.getString("client_email"));
        booking.setRoomNumber(rs.getInt("room_number"));
        booking.setRoomDescription(rs.getString("room_description"));
        booking.setBookingDate(rs.getDate("booking_date").toLocalDate());
        return booking;
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