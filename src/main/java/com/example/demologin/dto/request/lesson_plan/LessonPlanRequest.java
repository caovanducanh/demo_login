package com.example.demologin.dto.request.lesson_plan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonPlanRequest {
    @JsonProperty("teacher_id")
    private Long teacherId;
    @JsonProperty("grade_id")
    private Long gradeId;
    private String title;
    private String content;
    @JsonProperty("file_path")
    private String filePath; // có thể null nếu chưa upload file
}
