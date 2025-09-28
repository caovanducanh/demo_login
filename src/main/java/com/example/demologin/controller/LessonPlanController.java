package com.example.demologin.controller;

import com.example.demologin.annotation.SecuredEndpoint;
import com.example.demologin.annotation.SmartCache;
import com.example.demologin.dto.request.lesson_plan.LessonPlanRequest;
import com.example.demologin.dto.response.LessonPlanResponse;
import com.example.demologin.service.LessonPlanCompactionService;
import com.example.demologin.service.LessonPlanService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lesson-plans")
public class LessonPlanController {
    private final LessonPlanService lessonPlanService;
    private final LessonPlanCompactionService compactionService;

    public LessonPlanController(LessonPlanService lessonPlanService, LessonPlanCompactionService compactionService) {
        this.lessonPlanService = lessonPlanService;
        this.compactionService = compactionService;
    }

    @PostMapping
//    @SecuredEndpoint("LESSON_PLAN_CREATE")
    @SmartCache
    @Operation(summary = "Create new lesson plan",
            description = "Create a lesson plan")
    public LessonPlanResponse create(@RequestBody LessonPlanRequest req) {
        return lessonPlanService.createLessonPlan(req);
    }

    @PostMapping("/{lessonPlanId}/save")
    public ResponseEntity<String> compactAndSave(@PathVariable Long lessonPlanId) {
        try {
            compactionService.compactLessonPlan(lessonPlanId);
            return ResponseEntity.ok("Lesson plan " + lessonPlanId + " compacted successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to compact lesson plan " + lessonPlanId + ": " + e.getMessage());
        }
    }


}
