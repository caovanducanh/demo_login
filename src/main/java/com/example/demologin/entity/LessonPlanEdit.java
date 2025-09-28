package com.example.demologin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "lesson_plan_edits")
public class LessonPlanEdit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Liên kết tới LessonPlan
    @ManyToOne
    @JoinColumn(name = "lesson_plan_id", nullable = false)
    private LessonPlan lessonPlan;

    // Ai edit (nếu cần tracking user)
    @ManyToOne
    @JoinColumn(name = "editor_id")
    private User editor;

    // JSON lưu operation (insert, delete, update,...)
    @Column(columnDefinition = "JSON", nullable = false)
    private String operation;

    private LocalDateTime createdAt = LocalDateTime.now();
}
