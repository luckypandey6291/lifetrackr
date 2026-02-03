package com.lifetrackr.controller;

import com.lifetrackr.dto.CreateStudyTaskRequest;
import com.lifetrackr.dto.UpdateStudyTaskRequest;
import com.lifetrackr.model.StudyTask;
import com.lifetrackr.service.StudyTaskService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/study-tasks")
public class StudyTaskController {

    private final StudyTaskService studyTaskService;

    public StudyTaskController(StudyTaskService studyTaskService) {
        this.studyTaskService = studyTaskService;
    }

    /* ---------- GET ---------- */
    @GetMapping("/{userId}")
    public List<StudyTask> getTasks(@PathVariable String userId) {
        return studyTaskService.getTasksForUser(userId);
    }

    /* ---------- CREATE ---------- */
    @PostMapping
    public StudyTask create(@Valid @RequestBody CreateStudyTaskRequest req) {
        return studyTaskService.createTask(
                req.getUserId(),
                req.getSubject(),
                req.getTopic(),
                LocalDate.parse(req.getDueDate()),
                req.getPriority()
        );
    }

    /* ---------- UPDATE ---------- */
    @PutMapping("/{id}")
    public StudyTask update(
            @PathVariable String id,
            @Valid @RequestBody UpdateStudyTaskRequest req
    ) {
       return studyTaskService.updateTask(
        id,
        req.getSubject(),
        req.getTopic(),
        LocalDate.parse(req.getDueDate()),
        req.getPriority()
);
    }

    /* ---------- TOGGLE STATUS ---------- */
    @PatchMapping("/{id}/status")
    public StudyTask toggleStatus(@PathVariable String id) {
        return studyTaskService.toggleStatus(id);
    }

    /* ---------- GET TASK BY ID ---------- */
    @GetMapping("/task/{id}")
    public StudyTask getTaskById(@PathVariable String id) {
    return studyTaskService.getById(id)
            .orElseThrow(() -> new RuntimeException("Task not found"));
}


    /* ---------- DELETE ---------- */
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        studyTaskService.deleteById(id);
    }
}



