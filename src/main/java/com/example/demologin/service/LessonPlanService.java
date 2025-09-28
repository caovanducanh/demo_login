package com.example.demologin.service;

import com.example.demologin.dto.request.lesson_plan.LessonPlanRequest;
import com.example.demologin.dto.response.LessonPlanResponse;
import com.example.demologin.entity.LessonPlan;

public interface LessonPlanService {
    LessonPlanResponse createLessonPlan(LessonPlanRequest req);
    LessonPlanResponse mapToResponse(LessonPlan plan);
}
