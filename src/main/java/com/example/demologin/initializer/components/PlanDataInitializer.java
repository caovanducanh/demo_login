package com.example.demologin.initializer.components;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demologin.entity.Plan;
import com.example.demologin.repository.PlanRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Plan Data Initializer
 * 
 * Responsible for creating default subscription plans for the system.
 * These plans define different service tiers and pricing options.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class PlanDataInitializer {

    private final PlanRepository planRepository;

    @Transactional
    public void initializePlans() {
        log.info("💳 Initializing subscription plans...");
        
        if (planRepository.count() > 0) {
            log.info("ℹ️ Plans already exist, skipping plan initialization");
            return;
        }

        createDefaultPlans();
        
        log.info("✅ Successfully initialized {} subscription plans", planRepository.count());
    }

    private void createDefaultPlans() {
        log.debug("📋 Creating default subscription plans...");
        
        LocalDateTime now = LocalDateTime.now();
        
        List<Plan> plans = List.of(
            Plan.builder()
                .name("Gói Cơ Bản")
                .description("Gói cơ bản với các tính năng thiết yếu cho học sinh và giáo viên. " +
                           "Bao gồm truy cập các bài học cơ bản, làm bài kiểm tra và xem kết quả.")
                .price(0.0)
                .durationDays(30)
                .createdAt(now)
                .updatedAt(now)
                .build(),
                
            Plan.builder()
                .name("Gói Premium")
                .description("Gói premium với đầy đủ tính năng nâng cao. " +
                           "Bao gồm tất cả tính năng của gói cơ bản, thêm các bài học nâng cao, " +
                           "phân tích chi tiết kết quả, và hỗ trợ ưu tiên.")
                .price(199000.0)
                .durationDays(30)
                .createdAt(now)
                .updatedAt(now)
                .build(),
                
            Plan.builder()
                .name("Gói VIP")
                .description("Gói VIP với tất cả tính năng cao cấp và dịch vụ cá nhân hóa. " +
                           "Bao gồm tất cả tính năng của gói premium, thêm tư vấn một-một với giáo viên, " +
                           "tài liệu độc quyền, và không giới hạn truy cập.")
                .price(399000.0)
                .durationDays(30)
                .createdAt(now)
                .updatedAt(now)
                .build(),
                
            Plan.builder()
                .name("Gói Học Kỳ")
                .description("Gói đăng ký cho cả học kỳ với giá ưu đãi. " +
                           "Bao gồm tất cả tính năng premium trong 6 tháng, " +
                           "phù hợp cho việc học tập lâu dài.")
                .price(999000.0)
                .durationDays(180)
                .createdAt(now)
                .updatedAt(now)
                .build(),
                
            Plan.builder()
                .name("Gói Năm Học")
                .description("Gói đăng ký cho cả năm học với mức giá tốt nhất. " +
                           "Bao gồm tất cả tính năng VIP trong 12 tháng, " +
                           "tiết kiệm tối đa cho học sinh và phụ huynh.")
                .price(1799000.0)
                .durationDays(365)
                .createdAt(now)
                .updatedAt(now)
                .build(),
                
            Plan.builder()
                .name("Gói Giáo Viên")
                .description("Gói dành riêng cho giáo viên với các công cụ quản lý lớp học. " +
                           "Bao gồm tạo bài kiểm tra, quản lý học sinh, phân tích kết quả học tập, " +
                           "và công cụ hỗ trợ giảng dạy.")
                .price(299000.0)
                .durationDays(30)
                .createdAt(now)
                .updatedAt(now)
                .build()
        );

        planRepository.saveAll(plans);
        log.debug("✅ Created {} subscription plans", plans.size());
    }
}