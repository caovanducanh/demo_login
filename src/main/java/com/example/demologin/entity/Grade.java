package com.example.demologin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Grade {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "grade")
    private List<ClassEntity> classes = new ArrayList<>();

    @OneToMany(mappedBy = "grade")
    private List<LessonPlan> lessonPlans = new ArrayList<>();

    @OneToMany(mappedBy = "grade")
    private List<Exam> exams = new ArrayList<>();

    @ManyToMany(mappedBy = "grades")
    private List<Question> questions = new ArrayList<>();
}
