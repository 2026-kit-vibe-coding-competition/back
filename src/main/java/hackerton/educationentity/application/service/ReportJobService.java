package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.request.CreateAiRequestRequest;
import hackerton.educationentity.application.dto.request.CreateReportJobsRequest;
import hackerton.educationentity.application.dto.response.AiRequestResponse;
import hackerton.educationentity.application.dto.response.ReportJobStatusResponse;
import hackerton.educationentity.application.dto.response.ReportJobsCreateResponse;
import hackerton.educationentity.domain.ai_request.entity.AiRequest;
import hackerton.educationentity.domain.ai_request.repository.AiRequestRepository;
import hackerton.educationentity.domain.ai_request.type.AiRequestStatus;
import hackerton.educationentity.domain.evaluation.repository.EvaluationRepository;
import hackerton.educationentity.domain.evaluation.type.EvaluationStatus;
import hackerton.educationentity.domain.guideline.entity.Guideline;
import hackerton.educationentity.domain.guideline.repository.GuidelineRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportJobService {
    private static final int DEFAULT_UNDERSTANDING_LEVEL = 3;
    private static final int DEFAULT_ASSIGNMENT_LEVEL = 3;
    private static final String DEFAULT_TEACHER_MEMO = "Auto-generated from report-jobs request.";
    private static final String DEFAULT_OCR_SUMMARY = "No OCR summary provided.";

    private final AiRequestService aiRequestService;
    private final AiRequestRepository aiRequestRepository;
    private final GuidelineRepository guidelineRepository;
    private final EvaluationRepository evaluationRepository;

    @Transactional
    public ReportJobsCreateResponse createReportJobs(CreateReportJobsRequest request) {
        if (request == null || request.sessionId() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "session_id is required");
        }
        if (request.studentIds() == null || request.studentIds().isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "student_ids is required");
        }

        List<String> jobIds = new ArrayList<>();
        for (Long studentId : request.studentIds()) {
            if (studentId == null) {
                continue;
            }

            var evaluation = evaluationRepository
                    .findBySessionIdAndStudentIdAndStatusNot(request.sessionId(), studentId, EvaluationStatus.DELETED)
                    .orElse(null);

            AiRequestResponse aiRequest = aiRequestService.createAiRequest(
                    new CreateAiRequestRequest(
                            request.sessionId(),
                            studentId,
                            evaluation != null ? evaluation.getDataRef() : null,
                            DEFAULT_UNDERSTANDING_LEVEL,
                            DEFAULT_ASSIGNMENT_LEVEL,
                            List.of("follow-up"),
                            resolveTeacherMemo(evaluation),
                            DEFAULT_OCR_SUMMARY,
                            List.of((double) DEFAULT_ASSIGNMENT_LEVEL)
                    )
            );
            jobIds.add(aiRequest.jobId());
        }

        if (jobIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "No valid student_ids provided");
        }

        return new ReportJobsCreateResponse(jobIds);
    }

    public ReportJobStatusResponse getReportJobStatus(String jobId) {
        AiRequest aiRequest = aiRequestRepository.findByJobId(jobId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Report job not found"));

        Long guidelineId = guidelineRepository.findByAiRequestId(aiRequest.getId())
                .map(Guideline::getId)
                .orElse(null);

        return new ReportJobStatusResponse(
                aiRequest.getJobId(),
                aiRequest.getStudent().getId(),
                aiRequest.getSession().getId(),
                mapStatus(aiRequest.getStatus()),
                guidelineId,
                aiRequest.getErrorMessage(),
                aiRequest.getUpdatedAt()
        );
    }

    private String mapStatus(AiRequestStatus status) {
        if (status == null) {
            return "processing";
        }

        return switch (status) {
            case SUCCESS -> "completed";
            case FAILED -> "failed";
            case PARTIAL -> "processing";
        };
    }

    private String resolveTeacherMemo(hackerton.educationentity.domain.evaluation.entity.Evaluation evaluation) {
        if (evaluation == null || evaluation.getMemo() == null || evaluation.getMemo().isBlank()) {
            return DEFAULT_TEACHER_MEMO;
        }
        return evaluation.getMemo();
    }
}
