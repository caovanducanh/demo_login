package com.example.demologin.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private User teacher;

    @Column(columnDefinition = "TEXT")
    private String questionText;

    @Column(length = 20)
    private String type;

    @Column(length = 20)
    private String difficulty;

    @Column(columnDefinition = "TEXT")
    private String formula;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "question")
    private List<Option> options = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "question_grades",
            joinColumns = @JoinColumn(name = "question_id"),
            inverseJoinColumns = @JoinColumn(name = "grade_id")
    )
    private List<Grade> grades = new ArrayList<>();

    @ManyToMany(mappedBy = "questions")
    private List<Exam> exams = new ArrayList<>();
}

