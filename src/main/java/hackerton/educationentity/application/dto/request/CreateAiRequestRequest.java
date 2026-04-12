package hackerton.educationentity.application.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record CreateAiRequestRequest(
        @JsonAlias("session_id")
        Long sessionId,
        @JsonAlias("student_id")
        Long studentId,
        @JsonAlias("data_ref")
        String dataRef,
        @JsonAlias("understanding_level")
        Integer understandingLevel,
        @JsonAlias("assignment_level")
        Integer assignmentLevel,
        @JsonAlias("weakness_tags")
        List<String> weaknessTags,
        @JsonAlias("teacher_memo")
        String teacherMemo,
        @JsonAlias("ocr_summary")
        String ocrSummary,
        @JsonAlias("trend")
        List<Double> trend
) {
}
