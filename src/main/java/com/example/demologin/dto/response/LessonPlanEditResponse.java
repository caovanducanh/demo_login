package com.example.demologin.dto.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LessonPlanEditResponse {
    private Long id;
    private String operation;
    private LocalDateTime createdAt;
}
