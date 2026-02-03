package com.lifetrackr.controller;

import com.lifetrackr.dto.CreateCodingSessionRequest;
import jakarta.validation.Valid;

import com.lifetrackr.model.CodingSession;
import com.lifetrackr.service.CodingSessionService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/coding-sessions")
public class CodingSessionController {

    private final CodingSessionService codingSessionService;

    public CodingSessionController(CodingSessionService codingSessionService) {
        this.codingSessionService = codingSessionService;
    }

    @GetMapping("/{userId}")
    public List<CodingSession> getSessions(@PathVariable String userId) {
        return codingSessionService.getSessionsForUser(userId);
    }
    @PostMapping
    public CodingSession create(@Valid @RequestBody CreateCodingSessionRequest req) {
        return codingSessionService.createSession(
                req.getUserId(),
                req.getProblemName(),
                req.getPlatform(),
                req.getDifficulty(),
                req.getDurationMinutes(),
                req.getSolved()
        );
    }
}

