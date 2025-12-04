package com.example.health.service;

import com.example.health.domain.Routine;
import com.example.health.repository.RoutineRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoutineService {

    private final RoutineRepository repo;

    public RoutineService(RoutineRepository repo) {
        this.repo = repo;
    }

    public Routine save(Routine routine) {
        return repo.save(routine);
    }

    public List<Routine> findByUserId(Long userId) {
        return repo.findByUserId(userId);
    }

    public Routine update(Long id, Routine data) {
        Routine r = repo.findById(id).orElse(null);
        if (r == null) return null;

        r.setExerciseName(data.getExerciseName());
        r.setSets(data.getSets());
        r.setReps(data.getReps());
        r.setWeight(data.getWeight());
        r.setDate(data.getDate());

        return repo.save(r);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }
}
