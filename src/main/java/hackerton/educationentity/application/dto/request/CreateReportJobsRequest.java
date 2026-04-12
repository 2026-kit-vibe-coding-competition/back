package hackerton.educationentity.application.dto.request;

import com.fasterxml.jackson.annotation.JsonAlias;

import java.util.List;

public record CreateReportJobsRequest(
        @JsonAlias("session_id")
        Long sessionId,
        @JsonAlias("student_ids")
        List<Long> studentIds,
        @JsonAlias("trigger_source")
        String triggerSource
) {
}
