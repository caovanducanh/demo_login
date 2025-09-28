package com.example.demologin.initializer.components;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demologin.entity.Exam;
import com.example.demologin.entity.Grade;
import com.example.demologin.entity.Option;
import com.example.demologin.entity.Question;
import com.example.demologin.entity.User;
import com.example.demologin.repository.ExamRepository;
import com.example.demologin.repository.GradeRepository;
import com.example.demologin.repository.OptionRepository;
import com.example.demologin.repository.QuestionRepository;
import com.example.demologin.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Exam Data Initializer
 * 
 * Responsible for creating sample exam data including questions and options.
 * This must run after EducationDataInitializer and DefaultUserInitializer
 * since it depends on grades and teachers.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ExamDataInitializer {

    private final ExamRepository examRepository;
    private final QuestionRepository questionRepository;
    private final OptionRepository optionRepository;
    private final GradeRepository gradeRepository;
    private final UserRepository userRepository;

    @Transactional
    public void initializeExamData() {
        log.info("📝 Initializing exam data (questions, options, exams)...");
        
        initializeQuestions();
        initializeExams();
        
        log.info("✅ Successfully initialized exam data");
    }

    private void initializeQuestions() {
        log.debug("❓ Creating sample questions...");
        
        if (questionRepository.count() > 0) {
            log.info("ℹ️ Questions already exist, skipping question initialization");
            return;
        }

        List<Grade> grades = gradeRepository.findAll();
        Optional<User> adminUser = userRepository.findByUsername("admin"); // Sử dụng admin làm teacher mẫu
        
        if (grades.isEmpty() || adminUser.isEmpty()) {
            log.warn("⚠️ No grades or admin user found, cannot create questions");
            return;
        }

        User teacher = adminUser.get(); // Sử dụng admin làm teacher
        LocalDateTime now = LocalDateTime.now();

        // Tạo câu hỏi cho từng khối lớp
        for (Grade grade : grades) {
            createQuestionsForGrade(grade, teacher, now);
        }
        
        log.debug("✅ Created {} questions", questionRepository.count());
    }

    private void createQuestionsForGrade(Grade grade, User teacher, LocalDateTime now) {
        // Câu hỏi Toán học
        Question mathQuestion1 = Question.builder()
            .teacher(teacher)
            .questionText("Tính: 15 + 27 = ?")
            .type("MULTIPLE_CHOICE")
            .difficulty("EASY")
            .createdAt(now)
            .updatedAt(now)
            .grades(List.of(grade))
            .build();
        
        questionRepository.save(mathQuestion1);
        createOptionsForQuestion(mathQuestion1, List.of("42", "41", "43", "40"), 0);

        Question mathQuestion2 = Question.builder()
            .teacher(teacher)
            .questionText("Một hình chữ nhật có chiều dài 8cm, chiều rộng 5cm. Tính diện tích?")
            .type("MULTIPLE_CHOICE")
            .difficulty("MEDIUM")
            .createdAt(now)
            .updatedAt(now)
            .grades(List.of(grade))
            .build();
        
        questionRepository.save(mathQuestion2);
        createOptionsForQuestion(mathQuestion2, List.of("40 cm²", "39 cm²", "41 cm²", "38 cm²"), 0);

        // Câu hỏi Tiếng Việt
        Question vietnameseQuestion1 = Question.builder()
            .teacher(teacher)
            .questionText("Từ nào sau đây là danh từ?")
            .type("MULTIPLE_CHOICE")
            .difficulty("EASY")
            .createdAt(now)
            .updatedAt(now)
            .grades(List.of(grade))
            .build();
        
        questionRepository.save(vietnameseQuestion1);
        createOptionsForQuestion(vietnameseQuestion1, List.of("sách", "đọc", "nhanh", "đẹp"), 0);

        Question vietnameseQuestion2 = Question.builder()
            .teacher(teacher)
            .questionText("Câu nào sau đây là câu cảm thán?")
            .type("MULTIPLE_CHOICE")
            .difficulty("MEDIUM")
            .createdAt(now)
            .updatedAt(now)
            .grades(List.of(grade))
            .build();
        
        questionRepository.save(vietnameseQuestion2);
        createOptionsForQuestion(vietnameseQuestion2, 
            List.of("Trời hôm nay đẹp quá!", "Hôm nay trời đẹp.", "Trời có đẹp không?", "Hãy nhìn trời."), 0);

        // Câu hỏi Khoa học
        Question scienceQuestion1 = Question.builder()
            .teacher(teacher)
            .questionText("Nước sôi ở nhiệt độ bao nhiêu độ C?")
            .type("MULTIPLE_CHOICE")
            .difficulty("EASY")
            .createdAt(now)
            .updatedAt(now)
            .grades(List.of(grade))
            .build();
        
        questionRepository.save(scienceQuestion1);
        createOptionsForQuestion(scienceQuestion1, List.of("100°C", "90°C", "110°C", "80°C"), 0);

        Question scienceQuestion2 = Question.builder()
            .teacher(teacher)
            .questionText("Cơ quan nào của cây làm nhiệm vụ quang hợp?")
            .type("MULTIPLE_CHOICE")
            .difficulty("MEDIUM")
            .createdAt(now)
            .updatedAt(now)
            .grades(List.of(grade))
            .build();
        
        questionRepository.save(scienceQuestion2);
        createOptionsForQuestion(scienceQuestion2, List.of("Lá", "Thân", "Rễ", "Hoa"), 0);
    }

    private void createOptionsForQuestion(Question question, List<String> optionTexts, int correctIndex) {
        for (int i = 0; i < optionTexts.size(); i++) {
            Option option = Option.builder()
                .question(question)
                .optionText(optionTexts.get(i))
                .isCorrect(i == correctIndex)
                .build();
            
            optionRepository.save(option);
        }
    }

    private void initializeExams() {
        log.debug("📋 Creating sample exams...");
        
        if (examRepository.count() > 0) {
            log.info("ℹ️ Exams already exist, skipping exam initialization");
            return;
        }

        List<Grade> grades = gradeRepository.findAll();
        Optional<User> adminUser = userRepository.findByUsername("admin");
        
        if (grades.isEmpty() || adminUser.isEmpty()) {
            log.warn("⚠️ No grades or admin user found, cannot create exams");
            return;
        }

        User teacher = adminUser.get();
        LocalDateTime now = LocalDateTime.now();

        // Tạo bài kiểm tra cho từng khối lớp
        for (Grade grade : grades) {
            createExamsForGrade(grade, teacher, now);
        }
        
        log.debug("✅ Created {} exams", examRepository.count());
    }

    private void createExamsForGrade(Grade grade, User teacher, LocalDateTime now) {
        // Lấy tất cả câu hỏi và filter theo grade
        List<Question> allQuestions = questionRepository.findAll();
        List<Question> gradeQuestions = allQuestions.stream()
            .filter(q -> q.getGrades().contains(grade))
            .toList();
        
        if (gradeQuestions.isEmpty()) {
            log.warn("⚠️ No questions found for grade {}, cannot create exams", grade.getName());
            return;
        }

        // Tạo bài kiểm tra Toán học
        Exam mathExam = Exam.builder()
            .teacher(teacher)
            .grade(grade)
            .title("Kiểm tra Toán học - " + grade.getName())
            .description("Bài kiểm tra môn Toán học dành cho " + grade.getName())
            .difficulty("MEDIUM")
            .status("PUBLISHED")
            .approvedBy(teacher)
            .createdAt(now)
            .updatedAt(now)
            .questions(gradeQuestions.subList(0, Math.min(2, gradeQuestions.size()))) // Lấy 2 câu hỏi đầu
            .build();
        
        examRepository.save(mathExam);

        // Tạo bài kiểm tra Tiếng Việt
        if (gradeQuestions.size() >= 4) {
            Exam vietnameseExam = Exam.builder()
                .teacher(teacher)
                .grade(grade)
                .title("Kiểm tra Tiếng Việt - " + grade.getName())
                .description("Bài kiểm tra môn Tiếng Việt dành cho " + grade.getName())
                .difficulty("MEDIUM")
                .status("PUBLISHED")
                .approvedBy(teacher)
                .createdAt(now)
                .updatedAt(now)
                .questions(gradeQuestions.subList(2, 4)) // Lấy 2 câu hỏi tiếp theo
                .build();
            
            examRepository.save(vietnameseExam);
        }

        // Tạo bài kiểm tra Khoa học
        if (gradeQuestions.size() >= 6) {
            Exam scienceExam = Exam.builder()
                .teacher(teacher)
                .grade(grade)
                .title("Kiểm tra Khoa học - " + grade.getName())
                .description("Bài kiểm tra môn Khoa học dành cho " + grade.getName())
                .difficulty("MEDIUM")
                .status("PUBLISHED")
                .approvedBy(teacher)
                .createdAt(now)
                .updatedAt(now)
                .questions(gradeQuestions.subList(4, 6)) // Lấy 2 câu hỏi cuối
                .build();
            
            examRepository.save(scienceExam);
        }
    }
}