package com.example.demologin.initializer.components;

import com.example.demologin.entity.Branch;
import com.example.demologin.entity.BranchAllowedEmail;
import com.example.demologin.entity.Role;
import com.example.demologin.entity.User;
import com.example.demologin.enums.Gender;
import com.example.demologin.enums.UserStatus;
import com.example.demologin.repository.BranchRepository;
import com.example.demologin.repository.BranchAllowedEmailRepository;
import com.example.demologin.repository.RoleRepository;
import com.example.demologin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Branch Data Initializer Component
 * 
 * Initializes branch data including:
 * - HCM and HN campus branches
 * - Allowed email lists for each branch
 * - Test user for HCM branch
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class BranchDataInitializer {
    
    private final BranchRepository branchRepository;
    private final BranchAllowedEmailRepository branchAllowedEmailRepository;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    
    public void initializeBranchData() {
        log.info("🏢 Initializing Branch Data...");
        
        initializeBranches();
        initializeAllowedEmails();
        initializeTestUser();
        
        log.info("✅ Branch Data initialization completed");
    }
    
    private void initializeBranches() {
        // Create HCM branch
        if (branchRepository.findByCode("HCM").isEmpty()) {
            Branch hcmBranch = Branch.builder()
                    .name("Ho Chi Minh Campus")
                    .code("HCM")
                    .address("590 Cach Mang Thang Tam, District 3, Ho Chi Minh City")
                    .active(true)
                    .build();
            branchRepository.save(hcmBranch);
            log.info("✅ Created HCM branch");
        }
        
        // Create HN branch
        if (branchRepository.findByCode("HN").isEmpty()) {
            Branch hnBranch = Branch.builder()
                    .name("Ha Noi Campus")
                    .code("HN")
                    .address("Hoa Lac Hi-Tech Park, Km 29, Dai Mo, Thach That, Hanoi")
                    .active(true)
                    .build();
            branchRepository.save(hnBranch);
            log.info("✅ Created HN branch");
        }
    }
    
    private void initializeAllowedEmails() {
        // HCM branch allowed emails
        Branch hcmBranch = branchRepository.findByCode("HCM").orElse(null);
        if (hcmBranch != null) {
            addAllowedEmailIfNotExists(hcmBranch, "anhcvdse182894@fpt.edu.vn", "Test Student - Cao Van Duc Anh");
            addAllowedEmailIfNotExists(hcmBranch, "teacher1@fpt.edu.vn", "Teacher Account");
            addAllowedEmailIfNotExists(hcmBranch, "admin.hcm@fpt.edu.vn", "Admin Account");
            addAllowedEmailIfNotExists(hcmBranch, "student1@fe.edu.vn", "Student Account");
            addAllowedEmailIfNotExists(hcmBranch, "teacher1@fe.edu.vn", "Teacher Account");
            log.info("✅ Initialized allowed emails for HCM branch");
        }
        
        // HN branch allowed emails
        Branch hnBranch = branchRepository.findByCode("HN").orElse(null);
        if (hnBranch != null) {
            addAllowedEmailIfNotExists(hnBranch, "student.hn@fpt.edu.vn", "Student Account");
            addAllowedEmailIfNotExists(hnBranch, "teacher.hn@fpt.edu.vn", "Teacher Account");
            addAllowedEmailIfNotExists(hnBranch, "admin.hn@fpt.edu.vn", "Admin Account");
            addAllowedEmailIfNotExists(hnBranch, "student.hn@fe.edu.vn", "Student Account");
            addAllowedEmailIfNotExists(hnBranch, "teacher.hn@fe.edu.vn", "Teacher Account");
            log.info("✅ Initialized allowed emails for HN branch");
        }
    }
    
    private void addAllowedEmailIfNotExists(Branch branch, String email, String description) {
        if (branchAllowedEmailRepository.findByBranchAndEmail(branch, email).isEmpty()) {
            BranchAllowedEmail allowedEmail = BranchAllowedEmail.builder()
                    .branch(branch)
                    .email(email)
                    .description(description)
                    .active(true)
                    .build();
            branchAllowedEmailRepository.save(allowedEmail);
            log.debug("📧 Added allowed email {} for branch {}", email, branch.getCode());
        }
    }
    
    private void initializeTestUser() {
        String testEmail = "anhcvdse182894@fpt.edu.vn";
        
        if (userRepository.findByEmail(testEmail).isEmpty()) {
            // Get HCM branch
            Branch hcmBranch = branchRepository.findByCode("HCM")
                    .orElseThrow(() -> new RuntimeException("HCM branch not found"));
            
            // Get STUDENT role
            Role studentRole = roleRepository.findByName("STUDENT")
                    .orElseGet(() -> {
                        // Create STUDENT role if it doesn't exist
                        Role newRole = new Role();
                        newRole.setName("STUDENT");
                        return roleRepository.save(newRole);
                    });
            
            // Create test user
            User testUser = new User(
                    "anhcvdse182894",
                    passwordEncoder.encode("password123"),
                    "Cao Van Duc Anh",
                    testEmail,
                    "0901234567",
                    "Ho Chi Minh City"
            );
            
            testUser.setDateOfBirth(LocalDate.of(2000, 1, 1));
            testUser.setGender(Gender.MALE);
            testUser.setIdentityCard("123456789");
            testUser.setStatus(UserStatus.ACTIVE);
            testUser.setVerify(true);
            testUser.setBranch(hcmBranch);
            
            Set<Role> roles = new HashSet<>();
            roles.add(studentRole);
            testUser.setRoles(roles);
            
            userRepository.save(testUser);
            log.info("✅ Created test user: {} for HCM branch", testEmail);
        }
    }
}
