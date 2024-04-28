package com.task_helper.task_helper.controllers;

import com.task_helper.task_helper.services.StatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StatusController {

    private final StatusService statusService;

    @Autowired
    public StatusController(StatusService statusService) {this.statusService = statusService; }

    @PostMapping("/updateBookingCompletedStatus")
    public ResponseEntity<?> updateBookingCompletedStatus(@RequestParam("id") Long id, @RequestParam("bookingIsCompleted") boolean isCompleted) {
        try {
            boolean updateSuccess = statusService.updateBookingCompletedStatus(id, isCompleted);
            if (updateSuccess) {
                return ResponseEntity.ok("Status updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Error updating status: Unable to find the specified item");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating status: " + e.getMessage());
        }
    }

    @PostMapping("/updatePlaceDepartedStatus")
    public ResponseEntity<?> updatePlaceDepartedStatus(@RequestParam("id") Long id, @RequestParam("isDeparted") boolean isDeparted) {
        try {
            boolean updateSuccess = statusService.updatePlaceDepartedStatus(id, isDeparted);
            if (updateSuccess) {
                return ResponseEntity.ok("Status updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Error updating status: Unable to find the specified item");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating status: " + e.getMessage());
        }
    }

    @PostMapping("/updatePlaceArrivedStatus")
    public ResponseEntity<?> updatePlaceArrivedStatus(@RequestParam("id") Long id, @RequestParam("isArrived") boolean isArrived) {
        try {
            boolean updateSuccess = statusService.updatePlaceArrivedStatus(id, isArrived);
            if (updateSuccess) {
                return ResponseEntity.ok("Status updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Error updating status: Unable to find the specified item");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating status: " + e.getMessage());
        }
    }

    @PostMapping("/updateSubtaskCompletedStatus")
    public ResponseEntity<?> updateSubtaskCompletedStatus(@RequestParam("id") Long id, @RequestParam("subtaskIsCompleted") boolean isCompleted) {
        try {
            boolean updateStatus = statusService.updateSubtaskCompletedStatus(id, isCompleted);
            if (updateStatus) {
                return ResponseEntity.ok("Status updated successfully");
            } else {
                return ResponseEntity.badRequest().body("Error updating status: Unable to find the specified item");
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating status: " + e.getMessage());
        }
    }
}
