package com.example.demologin.service;

import com.example.demologin.dto.request.grade.GradeRequest;
import com.example.demologin.dto.response.GradeResponse;

import java.util.List;

public interface GradeService {
    GradeResponse createGrade(GradeRequest request);
    GradeResponse getGradeById(Long id);
    List<GradeResponse> getAllGrades();
    GradeResponse updateGrade(Long id, GradeRequest request);
    void deleteGrade(Long id);
}
