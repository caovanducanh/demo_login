package com.example.demologin.repository;

import com.example.demologin.entity.LessonPlan;
import com.example.demologin.entity.LessonPlanEdit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LessonPlanEditRepository extends JpaRepository<LessonPlanEdit, Long> {
    List<LessonPlanEdit> findByLessonPlanOrderByCreatedAtAsc(LessonPlan lessonPlan);
    void deleteByLessonPlan(LessonPlan lessonPlan);
    List<LessonPlanEdit> findByLessonPlanIdOrderByCreatedAtAsc(Long lessonPlanId);
}
