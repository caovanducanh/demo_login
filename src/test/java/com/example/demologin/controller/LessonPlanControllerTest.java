package com.example.demologin.controller;

import com.example.demologin.service.LessonPlanCompactionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class LessonPlanControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private LessonPlanCompactionService compactionService;

    @Test
    void testSaveLessonPlan_Success() throws Exception {
        Long lessonPlanId = 76L;

        // Giả lập compaction chạy OK
        doNothing().when(compactionService).compactLessonPlan(lessonPlanId);

        mockMvc.perform(post("/lesson-plans/{id}/save", lessonPlanId))
                .andExpect(status().isOk())
                .andExpect(content().string("Lesson plan 1 compacted successfully."));
    }

    @Test
    void testSaveLessonPlan_Failure() throws Exception {
        Long lessonPlanId = 2L;

        // Giả lập compaction bị lỗi
        doThrow(new RuntimeException("Compaction failed"))
                .when(compactionService).compactLessonPlan(lessonPlanId);

        mockMvc.perform(post("/lesson-plans/{id}/save", lessonPlanId))
                .andExpect(status().isInternalServerError())
                .andExpect(content().string("Failed to compact lesson plan 2: Compaction failed"));
    }
}
