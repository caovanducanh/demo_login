package com.example.demologin.repository;

import com.example.demologin.entity.ExamAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ExamAttemptRepository extends JpaRepository<ExamAttempt, Long> {
}
