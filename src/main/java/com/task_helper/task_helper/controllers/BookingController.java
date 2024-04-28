package com.task_helper.task_helper.controllers;

import com.task_helper.task_helper.entities.Booking;
import com.task_helper.task_helper.entities.Place;
import com.task_helper.task_helper.entities.Subtask;
import com.task_helper.task_helper.repositories.BookingRepository;
import com.task_helper.task_helper.services.BookingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
    public BookingController(BookingService bookingService) {
        this.bookingService = bookingService;
    }

    @GetMapping("/")
    public String showBookings(Model model) {
        List<Booking> bookings = bookingService.getBookingsByStatus(false);
        model.addAttribute("bookings", bookings);
        return "task-page";
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
            List<Place> sortedPlaces = booking.getPlaces()
                    .stream()
                    .sorted(Comparator.comparingInt(Place::getIndex))
                    .collect(Collectors.toList());

            booking.setPlaces(sortedPlaces);

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
