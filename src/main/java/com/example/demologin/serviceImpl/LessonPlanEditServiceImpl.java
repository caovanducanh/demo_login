package com.example.demologin.serviceImpl;

import com.example.demologin.dto.request.lesson_plan.LessonPlanEditRequest;
import com.example.demologin.dto.response.LessonPlanEditResponse;
import com.example.demologin.entity.LessonPlan;
import com.example.demologin.entity.LessonPlanEdit;
import com.example.demologin.entity.User;
import com.example.demologin.repository.LessonPlanEditRepository;
import com.example.demologin.repository.LessonPlanRepository;
import com.example.demologin.repository.UserRepository;
import com.example.demologin.service.LessonPlanEditService;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LessonPlanEditServiceImpl implements LessonPlanEditService {

    private final LessonPlanRepository lessonPlanRepo;
    private final LessonPlanEditRepository lessonPlanEditRepo;
    private final UserRepository userRepo;

    public LessonPlanEditServiceImpl(LessonPlanRepository lessonPlanRepo,
                                 LessonPlanEditRepository lessonPlanEditRepo,
                                 UserRepository userRepo) {
        this.lessonPlanRepo = lessonPlanRepo;
        this.lessonPlanEditRepo = lessonPlanEditRepo;
        this.userRepo = userRepo;
    }

    /**
     * Lưu 1 edit vào bảng lesson_plan_edits
     */
    @Override
    public LessonPlanEditResponse saveEdit(LessonPlanEditRequest req) {
        LessonPlan lessonPlan = lessonPlanRepo.findById(req.getLessonPlanId())
                .orElseThrow(() -> new IllegalArgumentException("LessonPlan not found"));

        User editor = null;
        if (req.getEditorId() != null) {
            editor = userRepo.findById(req.getEditorId())
                    .orElseThrow(() -> new IllegalArgumentException("Editor not found"));
        }

        LessonPlanEdit edit = new LessonPlanEdit();
        edit.setLessonPlan(lessonPlan);
        edit.setEditor(editor);
        edit.setOperation(req.getOperation());
        edit.setCreatedAt(LocalDateTime.now());

        LessonPlanEdit saved = lessonPlanEditRepo.save(edit);

        return mapToResponse(saved);
    }

    /**
     * Lấy danh sách edits cho 1 LessonPlan
     */
    @Override
    public List<LessonPlanEditResponse> getEdits(Long lessonPlanId) {
        LessonPlan lessonPlan = lessonPlanRepo.findById(lessonPlanId)
                .orElseThrow(() -> new IllegalArgumentException("LessonPlan not found"));

        return lessonPlanEditRepo.findByLessonPlanOrderByCreatedAtAsc(lessonPlan).stream()
                .map(this::mapToResponse)
                .toList();
    }

    private LessonPlanEditResponse mapToResponse(LessonPlanEdit edit) {
        LessonPlanEditResponse dto = new LessonPlanEditResponse();
        dto.setId(edit.getId());
        dto.setOperation(edit.getOperation());
        dto.setCreatedAt(edit.getCreatedAt());
        return dto;
    }
}

