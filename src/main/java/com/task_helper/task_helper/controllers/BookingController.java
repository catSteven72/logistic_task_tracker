package com.task_helper.task_helper.controllers;

import com.task_helper.task_helper.DTO.BookingUpdateDTO;
import com.task_helper.task_helper.entities.Booking;
import com.task_helper.task_helper.entities.Place;
import com.task_helper.task_helper.entities.Subtask;
import com.task_helper.task_helper.entities.User;
import com.task_helper.task_helper.repositories.BookingRepository;
import com.task_helper.task_helper.repositories.UserRepository;
import com.task_helper.task_helper.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class BookingController {

    private final BookingService bookingService;
    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    public BookingController(BookingService bookingService, UserRepository userRepository) {

        this.bookingService = bookingService;
        this.userRepository = userRepository;
    }

    private static class ApiResponse {
        private boolean success;
        private String message;

        public ApiResponse(boolean success, String message) {
            this.success = success;
            this.message = message;
        }
        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public void setSuccess(boolean success) { this.success = success; }
        public void setMessage(String message) { this.message = message; }
    }

    @GetMapping("/")
    public String showBookings(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        List<Booking> bookings = bookingService.getBookingsForCurrentUser(username);
        bookings.sort(Comparator.comparingLong(Booking::getId));
        bookings.forEach(booking -> booking.setPlaces(booking.getPlaces()
                .stream()
                .sorted(Comparator.comparingInt(Place::getIndex))
                .collect(Collectors.toList())));
        model.addAttribute("bookings", bookings);
        return "task-page";
    }

    @GetMapping("/update")
    public String update(@RequestParam("bookingId") Long bookingId,
                         Model model) {
        Booking booking = bookingService.getBookingById(bookingId);
        booking.setPlaces(booking.getPlaces()
                .stream()
                .sorted(Comparator.comparingInt(Place::getIndex))
                        .collect(Collectors.toList()));
        model.addAttribute("booking", booking);
        return "update-task";
    }

    @PostMapping("/update-booking")
    public ResponseEntity<?> updateBooking(@RequestBody BookingUpdateDTO bookingUpdateDTO) {
        try {
            bookingService.updateBooking(bookingUpdateDTO);
            return ResponseEntity.ok(new ApiResponse(true, "Booking updated successfully"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to update booking"));
        }
    }

    @PostMapping("/mark-booking-completed/{bookingId}")
    public ResponseEntity<?> markBookingCompleted(@PathVariable Long bookingId) {
        try {
            bookingService.markBookingCompleted(bookingId);
            return ResponseEntity.ok(new ApiResponse(true, "Booking marked as completed"));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ApiResponse(false, "Failed to mark booking as completed"));
        }
    }

    @GetMapping("/create")
    public ModelAndView createBookingForm() {
        ModelAndView modelAndView = new ModelAndView("create-task");
        modelAndView.addObject("booking", new Booking());
        return modelAndView;
    }

    @PostMapping("/create")
    public String createBooking(@ModelAttribute Booking booking, RedirectAttributes redirectAttributes) {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            User currentUser = userRepository.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("User not found"));

            List<Place> sortedPlaces = booking.getPlaces()
                    .stream()
                    .sorted(Comparator.comparingInt(Place::getIndex))
                    .collect(Collectors.toList());

            booking.setPlaces(sortedPlaces);
            booking.addUser(currentUser);

            for (Place place : booking.getPlaces()) {
                place.setBooking(booking);

                for (Subtask subtask : place.getSubtasks()) {
                    subtask.setPlace(place);
                }
            }
            bookingRepository.save(booking);
            return "redirect:/";
        } catch (Exception ex) {
            redirectAttributes.addFlashAttribute("error", "Failed to create booking due to: " + ex.getMessage());
            return "redirect:/create-task";
        }

    }

    @PostMapping("/mark-completed")
    public String markBookingAsCompleted(@RequestParam Map<String, String> allParams) {
        allParams.keySet().forEach(key -> {
            if (key.startsWith("booking_")) {
                Long bookingId = Long.parseLong(key.substring(5));
                bookingService.markBookingCompleted(bookingId);
            }
        });
        return "redirect:/";
    }
}
