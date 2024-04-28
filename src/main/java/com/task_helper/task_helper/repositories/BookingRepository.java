package com.task_helper.task_helper.repositories;

import com.task_helper.task_helper.entities.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findByCompleted(boolean completed);
}
