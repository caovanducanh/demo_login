package com.example.demologin.service;

import com.example.demologin.dto.request.lesson_plan.LessonPlanEditRequest;
import com.example.demologin.dto.response.LessonPlanEditResponse;

import java.util.List;

public interface LessonPlanEditService {
    LessonPlanEditResponse saveEdit(LessonPlanEditRequest req);
    List<LessonPlanEditResponse> getEdits(Long lessonPlanId);
}
