package com.example.demologin.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonPlanResponse {
    private Long id;
    private String title;
    private String content;
    private String filePath;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String teacherName; // optional, map từ User
    private String gradeName;   // optional, map từ Grade
}
