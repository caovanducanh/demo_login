package com.example.demologin.initializer.components;

import com.example.demologin.entity.Role;
import com.example.demologin.entity.User;
import com.example.demologin.enums.Gender;
import com.example.demologin.enums.UserStatus;
import com.example.demologin.repository.RoleRepository;
import com.example.demologin.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Default User Initializer
 * 
 * Responsible for creating default system users with appropriate roles.
 * This runs after PermissionRoleInitializer since users depend on roles.
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class DefaultUserInitializer {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public void initializeDefaultUsers() {
        log.info("👥 Initializing default system users...");
        
        if (userRepository.count() > 0) {
            log.info("ℹ️ Users already exist, skipping user initialization");
            return;
        }

        createDefaultUsers();
        
        log.info("✅ Successfully initialized {} default users", userRepository.count());
    }

    private void createDefaultUsers() {
        log.debug("👤 Creating default system users...");
        
        createUser("admin", "admin123", "ADMIN");
        createUser("member", "member123", "MEMBER");
        
        log.debug("✅ Created {} users", userRepository.count());
    }

    private void createUser(String username, String rawPassword, String roleName) {
        if (userRepository.findByUsername(username).isPresent()) {
            log.debug("⚠️ User '{}' already exists, skipping", username);
            return;
        }
        
        Role role = roleRepository.findByName(roleName)
            .orElseThrow(() -> new IllegalStateException("Role '" + roleName + "' not found. PermissionRoleInitializer must run first."));
        
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        
        User user = new User(
                username,
                passwordEncoder.encode(rawPassword),
                username + " Fullname",
                username + "@example.com",
                "0123456789",
                "123 Main Street"
        );
        
        // Set additional properties
        user.setRoles(roles);
        user.setIdentityCard("123456789");
        user.setDateOfBirth(LocalDate.of(1995, 1, 1));
        user.setStatus(UserStatus.ACTIVE);
        user.setGender(Gender.OTHER);
        user.setTokenVersion(0);
        user.setVerify(true);
        user.setLocked(false);
        
        userRepository.save(user);
        log.debug("✅ Created user '{}' with role '{}'", username, roleName);
    }
}
