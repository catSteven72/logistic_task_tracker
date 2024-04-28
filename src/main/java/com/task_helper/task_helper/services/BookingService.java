package com.task_helper.task_helper.services;

import com.task_helper.task_helper.entities.Booking;
import com.task_helper.task_helper.repositories.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository) {
        this.bookingRepository = bookingRepository;
    }

    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    public List<Booking> getBookingsByStatus(boolean completed) {
        return bookingRepository.findByCompleted(completed);
    }

    public void markBookingCompleted(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + bookingId));
        booking.setCompleted(true);
        bookingRepository.save(booking);
    }

    // add task
    // delete task
    // change task status
    // change sub task status
}
