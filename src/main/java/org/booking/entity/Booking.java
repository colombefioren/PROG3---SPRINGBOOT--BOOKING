package org.booking.entity;

import java.time.Instant;
import java.util.Objects;

public class Booking {
    private String clientName;
    private String clientPhoneNumber;
    private String clientEmail;
    private int roomNumber;
    private String roomDescription;
    private Instant bookingDate;

    public Booking() {
    }

    public Booking(String clientName, String clientPhoneNumber, String clientEmail, int roomNumber, String roomDescription, Instant bookingDate) {
        this.clientName = clientName;
        this.clientPhoneNumber = clientPhoneNumber;
        this.clientEmail = clientEmail;
        this.roomNumber = roomNumber;
        this.roomDescription = roomDescription;
        this.bookingDate = bookingDate;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getClientPhoneNumber() {
        return clientPhoneNumber;
    }

    public void setClientPhoneNumber(String clientPhoneNumber) {
        this.clientPhoneNumber = clientPhoneNumber;
    }

    public String getClientEmail() {
        return clientEmail;
    }

    public void setClientEmail(String clientEmail) {
        this.clientEmail = clientEmail;
    }

    public int getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(int roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomDescription() {
        return roomDescription;
    }

    public void setRoomDescription(String roomDescription) {
        this.roomDescription = roomDescription;
    }

    public Instant getBookingDate() {
        return bookingDate;
    }

    public void setBookingDate(Instant bookingDate) {
        this.bookingDate = bookingDate;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return roomNumber == booking.roomNumber && Objects.equals(clientName, booking.clientName) && Objects.equals(clientPhoneNumber, booking.clientPhoneNumber) && Objects.equals(clientEmail, booking.clientEmail) && Objects.equals(roomDescription, booking.roomDescription) && Objects.equals(bookingDate, booking.bookingDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(clientName, clientPhoneNumber, clientEmail, roomNumber, roomDescription, bookingDate);
    }

    @Override
    public String toString() {
        return "Booking{" +
                "clientName='" + clientName + '\'' +
                ", clientPhoneNumber='" + clientPhoneNumber + '\'' +
                ", clientEmail='" + clientEmail + '\'' +
                ", roomNumber=" + roomNumber +
                ", roomDescription='" + roomDescription + '\'' +
                ", bookingDate=" + bookingDate +
                '}';
    }
}
