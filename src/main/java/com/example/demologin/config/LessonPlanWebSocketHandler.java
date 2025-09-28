package com.example.demologin.config;

import com.example.demologin.dto.request.lesson_plan.LessonPlanEditRequest;
import com.example.demologin.dto.response.LessonPlanEditResponse;
import com.example.demologin.service.LessonPlanCompactionService;
import com.example.demologin.service.LessonPlanEditService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class LessonPlanWebSocketHandler extends TextWebSocketHandler {
    // Lưu các kết nối đang mở theo lessonPlanId
    private final Map<Long, Set<WebSocketSession>> sessionsByLessonPlan = new ConcurrentHashMap<>();

    private final LessonPlanEditService editService;
    private final LessonPlanCompactionService compactionService;

    public LessonPlanWebSocketHandler(LessonPlanEditService editService,
                                      LessonPlanCompactionService compactionService) {
        this.editService = editService;
        this.compactionService = compactionService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        Long lessonPlanId = getLessonPlanId(session);
        sessionsByLessonPlan.computeIfAbsent(lessonPlanId, k -> ConcurrentHashMap.newKeySet())
                .add(session);
        System.out.println("User joined lessonPlanId=" + lessonPlanId + ", current=" + sessionsByLessonPlan.get(lessonPlanId).size());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        LessonPlanEditRequest req = mapper.readValue(message.getPayload(), LessonPlanEditRequest.class);

        LessonPlanEditResponse saved = editService.saveEdit(req);

        Long lessonPlanId = req.getLessonPlanId();
        TextMessage broadcast = new TextMessage(mapper.writeValueAsString(saved));

        for (WebSocketSession s : sessionsByLessonPlan.getOrDefault(lessonPlanId, Set.of())) {
            if (s.isOpen() && !s.getId().equals(session.getId())) {
                s.sendMessage(broadcast);
            }
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        Long lessonPlanId = getLessonPlanId(session);
        Set<WebSocketSession> sessions = sessionsByLessonPlan.getOrDefault(lessonPlanId, Set.of());
        sessions.remove(session);

        System.out.println("User left lessonPlanId=" + lessonPlanId + ", remaining=" + sessions.size());

        // Nếu tất cả user rời khỏi thì trigger compaction
        if (sessions.isEmpty()) {
            System.out.println("No more users editing. Triggering compaction for lessonPlanId=" + lessonPlanId);
            compactionService.compactLessonPlan(lessonPlanId);
            sessionsByLessonPlan.remove(lessonPlanId);
        }
    }

    private Long getLessonPlanId(WebSocketSession session) {
        String query = Objects.requireNonNull(session.getUri()).getQuery(); // ví dụ: lessonPlanId=1
        for (String param : query.split("&")) {
            String[] kv = param.split("=");
            if (kv.length == 2 && kv[0].equals("lessonPlanId")) {
                return Long.valueOf(kv[1]);
            }
        }
        throw new IllegalArgumentException("lessonPlanId not provided in WebSocket URL");
    }
}
