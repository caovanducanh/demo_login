package com.example.demologin.serviceImpl;

import com.example.demologin.dto.request.grade.GradeRequest;
import com.example.demologin.dto.response.GradeResponse;
import com.example.demologin.entity.Grade;
import com.example.demologin.repository.GradeRepository;
import com.example.demologin.service.GradeService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class GradeServiceImpl implements GradeService {
    private final GradeRepository gradeRepository;

    public GradeServiceImpl(GradeRepository gradeRepository) {
        this.gradeRepository = gradeRepository;
    }

    private GradeResponse mapToResponse(Grade grade) {
        return GradeResponse.builder()
                .id(grade.getId())
                .name(grade.getName())
                .description(grade.getDescription())
                .build();
    }

    @Override
    public GradeResponse createGrade(GradeRequest request) {
        Grade grade = Grade.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
        Grade saved = gradeRepository.save(grade);
        return mapToResponse(saved);
    }

    @Override
    public GradeResponse getGradeById(Long id) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grade not found with id " + id));
        return mapToResponse(grade);
    }

    @Override
    public List<GradeResponse> getAllGrades() {
        return gradeRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public GradeResponse updateGrade(Long id, GradeRequest request) {
        Grade grade = gradeRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grade not found with id " + id));

        grade.setName(request.getName());
        grade.setDescription(request.getDescription());

        Grade updated = gradeRepository.save(grade);
        return mapToResponse(updated);
    }

    @Override
    public void deleteGrade(Long id) {
        if (!gradeRepository.existsById(id)) {
            throw new IllegalArgumentException("Grade not found with id " + id);
        }
        gradeRepository.deleteById(id);
    }
}
