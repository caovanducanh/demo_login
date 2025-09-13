package com.example.demologin.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "branches")
public class Branch {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String name;

    @Column(unique = true, nullable = false, length = 10)
    private String code;

    @Column(nullable = false, length = 255)
    private String address;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<User> users;

    @OneToMany(mappedBy = "branch", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<BranchAllowedEmail> allowedEmails;

    // Helper method to check if email is allowed for this branch
    public boolean isEmailAllowed(String email) {
        if (email == null || allowedEmails == null) {
            return false;
        }
        
        return allowedEmails.stream()
                .filter(allowedEmail -> allowedEmail.getActive())
                .anyMatch(allowedEmail -> allowedEmail.getEmail().equalsIgnoreCase(email.trim()));
    }
}
