package com.example.demologin.dto.request.lesson_plan;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonPlanEditRequest {
    @JsonProperty("lesson_plan_id")
    private Long lessonPlanId;
    @JsonProperty("editor_id")
    private Long editorId;   // optional
    private String operation; // JSON string mô tả edit
}
