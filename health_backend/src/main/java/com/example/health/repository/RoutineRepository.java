package com.example.health.repository;

import com.example.health.domain.Routine;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RoutineRepository extends JpaRepository<Routine, Long> {

    List<Routine> findByUserId(Long userId);

}
