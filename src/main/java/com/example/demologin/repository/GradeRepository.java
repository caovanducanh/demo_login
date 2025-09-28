package com.example.demologin.repository;

import com.example.demologin.entity.Grade;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface GradeRepository extends JpaRepository<Grade, Long> {
    List<Grade> findByNameContainingIgnoreCase(String keyword);
}
