package com.example.demologin.service;

import com.example.demologin.dto.response.LessonPlanResponse;

public interface LessonPlanCompactionService {
    void compactLessonPlan(Long lessonPlanId);
}
