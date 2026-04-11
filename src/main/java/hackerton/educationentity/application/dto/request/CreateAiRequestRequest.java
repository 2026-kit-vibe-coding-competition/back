package hackerton.educationentity.application.dto.request;

import java.util.List;

public record CreateAiRequestRequest(
        Long sessionId,
        Long studentId,
        String dataRef,
        Integer understandingLevel,
        Integer assignmentLevel,
        List<String> weaknessTags,
        String teacherMemo,
        String ocrSummary,
        List<Double> trend
) {
}
