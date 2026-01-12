package com.lifetrackr.controller;

import com.lifetrackr.dto.CreateWorkoutRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;

import com.lifetrackr.model.WorkoutEntry;
import com.lifetrackr.service.WorkoutService;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/workouts")
public class WorkoutController {

    private final WorkoutService workoutService;

    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping("/{userId}")
    public List<WorkoutEntry> getWorkouts(@PathVariable String userId) {
        return workoutService.getWorkoutsForUser(userId);
    }

    @PostMapping
    public WorkoutEntry create(@Valid @RequestBody CreateWorkoutRequest req) {
        return workoutService.createWorkout(
                req.getUserId(),
                req.getDescription(),
                req.getType().name(),
                req.getDurationMinutes(),
                LocalDate.parse(req.getDate())
        );
    }
}

