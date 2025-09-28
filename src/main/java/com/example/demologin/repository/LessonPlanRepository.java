package com.example.demologin.repository;

import com.example.demologin.entity.LessonPlan;
import com.example.demologin.entity.LessonPlanEdit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LessonPlanRepository extends JpaRepository<LessonPlan, Long> {

}
