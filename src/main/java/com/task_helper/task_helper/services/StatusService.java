package com.task_helper.task_helper.services;

import com.task_helper.task_helper.entities.Booking;
import com.task_helper.task_helper.entities.Place;
import com.task_helper.task_helper.entities.Subtask;
import com.task_helper.task_helper.repositories.BookingRepository;
import com.task_helper.task_helper.repositories.PlaceRepository;
import com.task_helper.task_helper.repositories.SubtaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class StatusService {
    private final BookingRepository bookingRepository;
    private final PlaceRepository placeRepository;
    private final SubtaskRepository subtaskRepository;

    @Autowired
    public StatusService(BookingRepository bookingRepository, PlaceRepository placeRepository, SubtaskRepository subtaskRepository) {
        this.bookingRepository = bookingRepository;
        this.placeRepository = placeRepository;
        this.subtaskRepository = subtaskRepository;
    }

    public boolean updateBookingCompletedStatus(Long bookingId, boolean isCompleted) {
        try {
            Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Invalid booking ID"));

            booking.setCompleted(isCompleted);
            bookingRepository.save(booking);
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    public boolean updatePlaceDepartedStatus(Long placeId, boolean isDeparted) {
        try {
            Place place = placeRepository.findById(placeId).orElseThrow(() -> new IllegalArgumentException("Invalid place ID"));
            place.setDeparted(isDeparted);
            placeRepository.save(place);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updatePlaceArrivedStatus(Long placeId, boolean isArrived) {
        try {
            Place place = placeRepository.findById(placeId).orElseThrow(() -> new IllegalArgumentException("Invalid place ID"));
            place.setArrived(isArrived);
            placeRepository.save(place);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean updateSubtaskCompletedStatus(Long subtaskId, boolean isCompleted) {
        try {
            Subtask subtask = subtaskRepository.findById(subtaskId).orElseThrow(() -> new IllegalArgumentException("Invalid subtask ID"));
            subtask.setCompleted(isCompleted);
            subtaskRepository.save(subtask);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
