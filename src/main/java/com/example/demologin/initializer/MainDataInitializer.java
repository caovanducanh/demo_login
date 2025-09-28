package com.example.demologin.initializer;

import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.example.demologin.initializer.components.ChapterDataInitializer;
import com.example.demologin.initializer.components.DefaultUserInitializer;
import com.example.demologin.initializer.components.EducationDataInitializer;
import com.example.demologin.initializer.components.ExamDataInitializer;
import com.example.demologin.initializer.components.PermissionRoleInitializer;
import com.example.demologin.initializer.components.PlanDataInitializer;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * Main Data Initializer - Orchestrates all initialization processes
 * 
 * This class coordinates the execution of all data initialization components
 * in the correct order to ensure system integrity and proper dependencies.
 * 
 * Execution Order:
 * 1. PermissionRoleInitializer - Creates permissions and roles
 * 2. DefaultUserInitializer - Creates default users with assigned roles
 * 3. PlanDataInitializer - Creates subscription plans
 * 4. EducationDataInitializer - Creates grades, classes, and lesson plans
 * 5. ChapterDataInitializer - Creates chapters for lesson plans
 * 6. ExamDataInitializer - Creates questions, options, and exams
 * 7. Future initializers can be added here with proper ordering
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Order(1) // Ensure this runs first among all CommandLineRunners
public class MainDataInitializer implements CommandLineRunner {

    private final PermissionRoleInitializer permissionRoleInitializer;
    private final DefaultUserInitializer defaultUserInitializer;
    private final PlanDataInitializer planDataInitializer;
    private final EducationDataInitializer educationDataInitializer;
    private final ChapterDataInitializer chapterDataInitializer;
    private final ExamDataInitializer examDataInitializer;

    @Override
    public void run(String... args) throws Exception {
        log.info("🚀 Starting Main Data Initialization Process...");
        
        try {
            // Step 1: Initialize Permissions and Roles
            log.info("📋 Step 1: Initializing Permissions and Roles...");
            permissionRoleInitializer.initializePermissionsAndRoles();
            log.info("✅ Permissions and Roles initialization completed");
            
            // Step 2: Initialize Default Users
            log.info("👥 Step 2: Initializing Default Users...");
            defaultUserInitializer.initializeDefaultUsers();
            log.info("✅ Default Users initialization completed");
            
            // Step 3: Initialize Subscription Plans
            log.info("💳 Step 3: Initializing Subscription Plans...");
            planDataInitializer.initializePlans();
            log.info("✅ Subscription Plans initialization completed");
            
            // Step 4: Initialize Education Data
            log.info("📚 Step 4: Initializing Education Data (Grades, Classes, Lesson Plans)...");
            educationDataInitializer.initializeEducationData();
            log.info("✅ Education Data initialization completed");
            
            // Step 5: Initialize Chapters
            log.info("📖 Step 5: Initializing Chapters...");
            chapterDataInitializer.initializeChapters();
            log.info("✅ Chapters initialization completed");
            
            // Step 6: Initialize Exam Data
            log.info("📝 Step 6: Initializing Exam Data (Questions, Options, Exams)...");
            examDataInitializer.initializeExamData();
            log.info("✅ Exam Data initialization completed");
            
            // Future initialization steps can be added here
            // Example:
            // log.info("📊 Step 7: Initializing System Settings...");
            // systemSettingsInitializer.initializeSettings();
            
            log.info("🎉 Main Data Initialization Process completed successfully!");
            
        } catch (Exception e) {
            log.error("❌ Error during data initialization: {}", e.getMessage(), e);
            throw e; // Re-throw to prevent application startup with incomplete data
        }
    }
}
