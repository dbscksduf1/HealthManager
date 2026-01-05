package com.example.health.service;

import com.example.health.domain.Routine;
import com.example.health.repository.RoutineRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class RoutineService {

    private final RoutineRepository repo;

    // ✅ BMI 캐시 (키: height-weight, 값: bmi)
    private final Map<String, Double> bmiCache = new ConcurrentHashMap<>();

    public RoutineService(RoutineRepository repo) {
        this.repo = repo;
    }

    /* ======================
       기존 Routine CRUD 로직
       ====================== */

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

    /* ======================
       ✅ BMI 계산 + 캐싱 로직
       ====================== */

    public double calculateBmi(double height, double weight) {

        // 캐시 키 생성
        String cacheKey = height + "-" + weight;

        long start = System.currentTimeMillis();

        double bmi = bmiCache.computeIfAbsent(cacheKey, key ->
                weight / Math.pow(height / 100.0, 2)
        );

        long end = System.currentTimeMillis();
        System.out.println("BMI 계산 응답시간 = " + (end - start) + "ms");

        return bmi;
    }
}
