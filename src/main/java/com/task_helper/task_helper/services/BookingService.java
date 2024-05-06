package com.task_helper.task_helper.services;

import com.task_helper.task_helper.DTO.BookingUpdateDTO;
import com.task_helper.task_helper.DTO.PlaceDTO;
import com.task_helper.task_helper.DTO.SubtaskDTO;
import com.task_helper.task_helper.entities.Booking;
import com.task_helper.task_helper.entities.Place;
import com.task_helper.task_helper.entities.Subtask;
import com.task_helper.task_helper.entities.User;
import com.task_helper.task_helper.repositories.BookingRepository;
import com.task_helper.task_helper.repositories.PlaceRepository;
import com.task_helper.task_helper.repositories.SubtaskRepository;
import com.task_helper.task_helper.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookingService {

    private final BookingRepository bookingRepository;
    private final PlaceRepository placeRepository;
    private final SubtaskRepository subtaskRepository;
    private final UserRepository userRepository;

    @Autowired
    public BookingService(BookingRepository bookingRepository,
                          PlaceRepository placeRepository,
                          SubtaskRepository subtaskRepository,
                          UserRepository userRepository) {
        this.bookingRepository = bookingRepository;
        this.placeRepository = placeRepository;
        this.subtaskRepository = subtaskRepository;
        this.userRepository = userRepository;
    }


    public List<Booking> getAllBookings() {
        return bookingRepository.findAll();
    }

    @Transactional
    public void assignUserToBooking(Long userId, Long bookingId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Booking booking = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new IllegalArgumentException("Booking not found"));

        booking.addUser(user);
        bookingRepository.save(booking);
    }


    @Transactional
    public Booking convertToEntity(BookingUpdateDTO bookingUpdateDTO) {
        Long bookingId = bookingUpdateDTO.getBookingId();
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + bookingId));
        booking.setName(bookingUpdateDTO.getBookingName());
        booking.setDescription(bookingUpdateDTO.getBookingDescription());

        if (bookingUpdateDTO.getPlaces() != null) {
            List<Place> places = bookingUpdateDTO.getPlaces()
                    .stream()
                    .map(placeDTO -> convertPlaceDTOtoPlace(placeDTO, booking))
                    .collect((Collectors.toList()));
            booking.getPlaces().clear();
            booking.getPlaces().addAll(places);
        }
        return booking;
    }
    @Transactional
    private Place convertPlaceDTOtoPlace(PlaceDTO placeDTO, Booking booking) {
        Long placeId = placeDTO.getId();
        Place place;

        Optional<Place> existingPlace = placeRepository.findById(placeId);

        if (existingPlace.isPresent()) {
            place = existingPlace.get();
        } else {
            place = new Place();
        }

        place.setCity(placeDTO.getCity());
        place.setCountry(placeDTO.getCountry());
        place.setIndex(placeDTO.getIndex());
        place.setForwarder(placeDTO.getForwarder());
        place.setBooking(booking);

        if (placeDTO.getSubtasks() != null) {
            List<Subtask> subtasks = placeDTO.getSubtasks()
                    .stream()
                    .map(subtaskDTO -> convertSubtaskDTOtoSubtask(subtaskDTO, place))
                    .collect(Collectors.toList());
            place.getSubtasks().clear();
            place.getSubtasks().addAll(subtasks);
        }

        return place;
    }
    @Transactional
    private Subtask convertSubtaskDTOtoSubtask(SubtaskDTO subtaskDTO, Place place) {
        Long subtaskId = subtaskDTO.getId();
        Subtask subtask;

        Optional<Subtask> existingSubtask = subtaskRepository.findById(subtaskId);

        if (existingSubtask.isPresent()) {
            subtask = existingSubtask.get();
        } else {
            subtask = new Subtask();
        }

        subtask.setName(subtaskDTO.getName());
        subtask.setDescription(subtaskDTO.getDescription());
        subtask.setPlace(place);

        return subtask;
    }

    public Booking getBookingById(Long bookingId) {
        return bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + bookingId));
    }

    public List<Booking> getBookingsByStatus(boolean completed) {
        return bookingRepository.findByCompleted(completed);
    }

    public List<Booking> getBookingsForCurrentUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));
        return bookingRepository.findByUsersContainingAndCompletedFalse(user);
    }

    public void markBookingCompleted(Long bookingId) {
        Booking booking = bookingRepository.findById(bookingId).orElseThrow(() -> new IllegalArgumentException("Invalid booking Id:" + bookingId));
        booking.setCompleted(!booking.isCompleted());
        bookingRepository.save(booking);
    }

    public void updateBooking(BookingUpdateDTO bookingUpdateDTO) {
        Booking booking = convertToEntity(bookingUpdateDTO);
        bookingRepository.save(booking);
    }

    public void updatePlace(Long placeId, String city, String country, Short index, String forwarder, List<Subtask> subtasks) {
        Place place = placeRepository.findById(placeId).orElseThrow(() -> new IllegalArgumentException("Invalid place Id:" + placeId));
        place.setCity(city);
        place.setCountry(country);
        place.setIndex(index);
        place.setForwarder(forwarder);
        place.setSubtasks(subtasks);
        placeRepository.save(place);
    }

    public void updateSubtask(Long subtaskId, String name, String description) {
        Subtask subtask = subtaskRepository.findById(subtaskId).orElseThrow(() -> new IllegalArgumentException("Invalid subtask Id:" + subtaskId));
        subtask.setName(name);
        subtask.setDescription(description);
        subtaskRepository.save(subtask);
    }
}
