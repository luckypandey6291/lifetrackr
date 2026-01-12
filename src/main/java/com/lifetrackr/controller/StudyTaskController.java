package com.lifetrackr.controller;

import com.lifetrackr.dto.CreateStudyTaskRequest;
import jakarta.validation.Valid;
import java.time.LocalDate;

import com.lifetrackr.model.StudyTask;
import com.lifetrackr.service.StudyTaskService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/study-tasks")
public class StudyTaskController {

    private final StudyTaskService studyTaskService;

    public StudyTaskController(StudyTaskService studyTaskService) {
        this.studyTaskService = studyTaskService;
    }

    @GetMapping("/{userId}")
    public List<StudyTask> getTasks(@PathVariable String userId) {
        return studyTaskService.getTasksForUser(userId);
    }

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
    @DeleteMapping("/{id}")
    public void delete(@PathVariable String id) {
        studyTaskService.deleteById(id);
    }

}


