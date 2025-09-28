//package com.example.demologin.controller;
//
//import com.example.demologin.dto.response.LessonPlanResponse;
//import com.example.demologin.service.LessonPlanCompactionService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//@RequestMapping("/api/lesson-plans")
//public class LessonPlanCompactionController {
//
//    private final LessonPlanCompactionService compactionService;
//
//
//    public LessonPlanCompactionController(LessonPlanCompactionService compactionService) {
//        this.compactionService = compactionService;
//    }
//
//    @PostMapping("/{lessonPlanId}/save")
//    public ResponseEntity<LessonPlanResponse> compactAndSave(@PathVariable Long lessonPlanId) {
//        return ResponseEntity.ok(compactionService.compactLessonPlan(lessonPlanId));
//    }
//}