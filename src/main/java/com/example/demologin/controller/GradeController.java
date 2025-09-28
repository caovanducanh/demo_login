package com.example.demologin.controller;

import com.example.demologin.dto.request.grade.GradeRequest;
import com.example.demologin.dto.response.GradeResponse;
import com.example.demologin.service.GradeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grades")
public class GradeController {
    private final GradeService gradeService;

    public GradeController(GradeService gradeService) {
        this.gradeService = gradeService;
    }

    @PostMapping
    public ResponseEntity<GradeResponse> create(@RequestBody GradeRequest request) {
        return ResponseEntity.ok(gradeService.createGrade(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GradeResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(gradeService.getGradeById(id));
    }

    @GetMapping
    public ResponseEntity<List<GradeResponse>> getAll() {
        return ResponseEntity.ok(gradeService.getAllGrades());
    }

    @PutMapping("/{id}")
    public ResponseEntity<GradeResponse> update(@PathVariable Long id, @RequestBody GradeRequest request) {
        return ResponseEntity.ok(gradeService.updateGrade(id, request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        gradeService.deleteGrade(id);
        return ResponseEntity.noContent().build();
    }
}
