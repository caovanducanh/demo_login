package com.example.demologin.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Chapter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "lesson_plan_id")
    private LessonPlan lessonPlan;

    @Column(length = 100)
    private String name;

    private Integer orderNo;
}

