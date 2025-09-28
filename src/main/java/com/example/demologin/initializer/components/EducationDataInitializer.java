package com.example.demologin.initializer.components;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demologin.entity.ClassEntity;
import com.example.demologin.entity.Grade;
import com.example.demologin.entity.LessonPlan;
import com.example.demologin.repository.ClassEntityRepository;
import com.example.demologin.repository.GradeRepository;
import com.example.demologin.repository.LessonPlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Education Data Initializer
 * 
 * Responsible for creating default education data including grades, classes, and lesson plans.
 * This provides basic academic structure for the system.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class EducationDataInitializer {

    private final GradeRepository gradeRepository;
    private final ClassEntityRepository classEntityRepository;
    private final LessonPlanRepository lessonPlanRepository;

    @Transactional
    public void initializeEducationData() {
        log.info("📚 Initializing education data (grades, classes, lesson plans)...");
        
        initializeGrades();
        initializeClasses();
        initializeLessonPlans();
        
        log.info("✅ Successfully initialized education data");
    }

    private void initializeGrades() {
        log.debug("🎓 Creating default grades...");
        
        if (gradeRepository.count() > 0) {
            log.info("ℹ️ Grades already exist, skipping grade initialization");
            return;
        }

        List<Grade> grades = List.of(
            Grade.builder()
                .name("Lớp 1")
                .description("Lớp học dành cho học sinh năm thứ nhất")
                .build(),
            Grade.builder()
                .name("Lớp 2")
                .description("Lớp học dành cho học sinh năm thứ hai")
                .build(),
            Grade.builder()
                .name("Lớp 3")
                .description("Lớp học dành cho học sinh năm thứ ba")
                .build(),
            Grade.builder()
                .name("Lớp 4")
                .description("Lớp học dành cho học sinh năm thứ tư")
                .build(),
            Grade.builder()
                .name("Lớp 5")
                .description("Lớp học dành cho học sinh năm thứ năm")
                .build(),
            Grade.builder()
                .name("Lớp 6")
                .description("Lớp học dành cho học sinh năm thứ sáu")
                .build(),
            Grade.builder()
                .name("Lớp 7")
                .description("Lớp học dành cho học sinh năm thứ bảy")
                .build(),
            Grade.builder()
                .name("Lớp 8")
                .description("Lớp học dành cho học sinh năm thứ tám")
                .build(),
            Grade.builder()
                .name("Lớp 9")
                .description("Lớp học dành cho học sinh năm thứ chín")
                .build(),
            Grade.builder()
                .name("Lớp 10")
                .description("Lớp học dành cho học sinh năm thứ mười")
                .build(),
            Grade.builder()
                .name("Lớp 11")
                .description("Lớp học dành cho học sinh năm thứ mười một")
                .build(),
            Grade.builder()
                .name("Lớp 12")
                .description("Lớp học dành cho học sinh năm thứ mười hai")
                .build()
        );

        gradeRepository.saveAll(grades);
        log.debug("✅ Created {} grades", grades.size());
    }

    private void initializeClasses() {
        log.debug("🏫 Creating default classes...");
        
        if (classEntityRepository.count() > 0) {
            log.info("ℹ️ Classes already exist, skipping class initialization");
            return;
        }

        List<Grade> grades = gradeRepository.findAll();
        if (grades.isEmpty()) {
            log.warn("⚠️ No grades found, cannot create classes");
            return;
        }

        // Tạo 2 lớp cho mỗi khối lớp
        for (Grade grade : grades) {
            for (int i = 1; i <= 2; i++) {
                ClassEntity classEntity = ClassEntity.builder()
                    .name(grade.getName() + "A" + i)
                    .grade(grade)
                    .createdAt(LocalDateTime.now())
                    .build();
                
                classEntityRepository.save(classEntity);
            }
        }
        
        log.debug("✅ Created {} classes", classEntityRepository.count());
    }

    private void initializeLessonPlans() {
        log.debug("📋 Creating default lesson plans...");
        
        if (lessonPlanRepository.count() > 0) {
            log.info("ℹ️ Lesson plans already exist, skipping lesson plan initialization");
            return;
        }

        List<Grade> grades = gradeRepository.findAll();
        if (grades.isEmpty()) {
            log.warn("⚠️ No grades found, cannot create lesson plans");
            return;
        }

        // Tạo kế hoạch bài học mẫu cho từng khối lớp
        String[] subjects = {"Toán học", "Tiếng Việt", "Khoa học", "Lịch sử", "Địa lý", "Tiếng Anh"};
        
        for (Grade grade : grades) {
            for (String subject : subjects) {
                LessonPlan lessonPlan = LessonPlan.builder()
                    .title("Kế hoạch " + subject + " - " + grade.getName())
                    .content("Kế hoạch giảng dạy môn " + subject + " dành cho " + grade.getName() + ". " +
                           "Nội dung chi tiết kế hoạch giảng dạy môn " + subject + " cho " + grade.getName())
                    .grade(grade)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
                
                lessonPlanRepository.save(lessonPlan);
            }
        }
        
        log.debug("✅ Created {} lesson plans", lessonPlanRepository.count());
    }
}