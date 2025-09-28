package com.example.demologin.initializer.components;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demologin.entity.Chapter;
import com.example.demologin.entity.LessonPlan;
import com.example.demologin.repository.ChapterRepository;
import com.example.demologin.repository.LessonPlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Chapter Data Initializer
 * 
 * Responsible for creating chapters for lesson plans.
 * This must run after EducationDataInitializer since it depends on lesson plans.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ChapterDataInitializer {

    private final ChapterRepository chapterRepository;
    private final LessonPlanRepository lessonPlanRepository;

    @Transactional
    public void initializeChapters() {
        log.info("📖 Initializing chapters...");
        
        if (chapterRepository.count() > 0) {
            log.info("ℹ️ Chapters already exist, skipping chapter initialization");
            return;
        }

        createChaptersForLessonPlans();
        
        log.info("✅ Successfully initialized {} chapters", chapterRepository.count());
    }

    private void createChaptersForLessonPlans() {
        log.debug("📚 Creating chapters for lesson plans...");
        
        List<LessonPlan> lessonPlans = lessonPlanRepository.findAll();
        
        if (lessonPlans.isEmpty()) {
            log.warn("⚠️ No lesson plans found, cannot create chapters");
            return;
        }

        for (LessonPlan lessonPlan : lessonPlans) {
            createChaptersForLessonPlan(lessonPlan);
        }
        
        log.debug("✅ Created chapters for {} lesson plans", lessonPlans.size());
    }

    private void createChaptersForLessonPlan(LessonPlan lessonPlan) {
        String subject = extractSubjectFromTitle(lessonPlan.getTitle());
        
        List<String> chapterNames = getChapterNamesForSubject(subject);
        
        for (int i = 0; i < chapterNames.size(); i++) {
            Chapter chapter = Chapter.builder()
                .lessonPlan(lessonPlan)
                .name(chapterNames.get(i))
                .orderNo(i + 1)
                .build();
            
            chapterRepository.save(chapter);
        }
    }

    private String extractSubjectFromTitle(String title) {
        if (title.contains("Toán học")) return "Toán học";
        if (title.contains("Tiếng Việt")) return "Tiếng Việt";
        if (title.contains("Khoa học")) return "Khoa học";
        if (title.contains("Lịch sử")) return "Lịch sử";
        if (title.contains("Địa lý")) return "Địa lý";
        if (title.contains("Tiếng Anh")) return "Tiếng Anh";
        return "Tổng hợp";
    }

    private List<String> getChapterNamesForSubject(String subject) {
        return switch (subject) {
            case "Toán học" -> List.of(
                "Chương 1: Số tự nhiên và các phép tính",
                "Chương 2: Hình học cơ bản",
                "Chương 3: Đo lường và đơn vị",
                "Chương 4: Bài toán có lời văn",
                "Chương 5: Thống kê và xác suất đơn giản"
            );
            case "Tiếng Việt" -> List.of(
                "Chương 1: Tập đọc",
                "Chương 2: Chính tả",
                "Chương 3: Luyện từ và câu",
                "Chương 4: Tập làm văn",
                "Chương 5: Ngữ pháp cơ bản"
            );
            case "Khoa học" -> List.of(
                "Chương 1: Thế giới xung quanh ta",
                "Chương 2: Con người và sức khỏe",
                "Chương 3: Động vật và thực vật",
                "Chương 4: Đất, nước, không khí",
                "Chương 5: Vật chất và năng lượng"
            );
            case "Lịch sử" -> List.of(
                "Chương 1: Lịch sử cổ đại",
                "Chương 2: Các triều đại phong kiến",
                "Chương 3: Thời kỳ cận đại",
                "Chương 4: Lịch sử hiện đại",
                "Chương 5: Truyền thống và văn hóa"
            );
            case "Địa lý" -> List.of(
                "Chương 1: Bản đồ và định hướng",
                "Chương 2: Khí hậu và thời tiết",
                "Chương 3: Địa hình Việt Nam",
                "Chương 4: Tài nguyên thiên nhiên",
                "Chương 5: Dân cư và hoạt động kinh tế"
            );
            case "Tiếng Anh" -> List.of(
                "Chapter 1: Greetings and Introductions",
                "Chapter 2: Family and Friends",
                "Chapter 3: School and Learning",
                "Chapter 4: Daily Activities",
                "Chapter 5: Hobbies and Interests"
            );
            default -> List.of(
                "Chương 1: Kiến thức cơ bản",
                "Chương 2: Luyện tập và thực hành",
                "Chương 3: Ứng dụng thực tế"
            );
        };
    }
}