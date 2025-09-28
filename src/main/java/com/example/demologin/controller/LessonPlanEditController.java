package com.example.demologin.controller;

import com.example.demologin.annotation.SecuredEndpoint;
import com.example.demologin.annotation.SmartCache;
import com.example.demologin.dto.request.lesson_plan.LessonPlanEditRequest;
import com.example.demologin.dto.response.LessonPlanEditResponse;
import com.example.demologin.service.LessonPlanEditService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lesson-plan-edits")
public class LessonPlanEditController {
    private final LessonPlanEditService editService;

    public LessonPlanEditController(LessonPlanEditService editService) {
        this.editService = editService;
    }

    @PostMapping
//    @SecuredEndpoint("LESSON_PLAN_EDIT_CREATE")
    @SmartCache
    @Operation(summary = "Create new lesson plan edit",
            description = "Create a lesson plan edit record")
    public LessonPlanEditResponse create(@RequestBody LessonPlanEditRequest req) {
        return editService.saveEdit(req);
    }

    @GetMapping("/{lessonPlanId}")
//    @SecuredEndpoint("LESSON_PLAN_EDIT_VIEW")
    @SmartCache
    @Operation(summary = "Create new lesson plan",
            description = "Create a lesson plan")
    public List<LessonPlanEditResponse> getAll(@PathVariable Long lessonPlanId) {
        return editService.getEdits(lessonPlanId);
    }
}
