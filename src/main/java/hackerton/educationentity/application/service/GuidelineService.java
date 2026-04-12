package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.request.ApproveGuidelineRequest;
import hackerton.educationentity.application.dto.request.ReviewGuidelineRequest;
import hackerton.educationentity.application.dto.request.ShareGuidelineRequest;
import hackerton.educationentity.application.dto.response.GuidelineResponse;
import hackerton.educationentity.domain.feedback_report.entity.FeedbackReport;
import hackerton.educationentity.domain.feedback_report.repository.FeedbackReportRepository;
import hackerton.educationentity.domain.guideline.entity.Guideline;
import hackerton.educationentity.domain.guideline.repository.GuidelineRepository;
import hackerton.educationentity.domain.guideline.type.GuidelineAudience;
import hackerton.educationentity.domain.guideline.type.GuidelineStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GuidelineService {
    private final GuidelineRepository guidelineRepository;
    private final FeedbackReportRepository feedbackReportRepository;

    public GuidelineResponse getGuideline(Long id) {
        return toResponse(getGuidelineEntity(id));
    }

    public List<GuidelineResponse> getGuidelinesByAudience(GuidelineAudience audience) {
        return (audience == null ? guidelineRepository.findAll() : guidelineRepository.findByAudience(audience)).stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional
    public GuidelineResponse reviewGuideline(Long id, ReviewGuidelineRequest request) {
        Guideline guideline = getGuidelineEntity(id);
        if (guideline.getStatus() == GuidelineStatus.SHARED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Shared guideline cannot be moved back to review");
        }
        if (request != null && request.teacherReviewNote() != null) {
            guideline.setTeacherReviewNote(request.teacherReviewNote());
        }
        guideline.setStatus(GuidelineStatus.REVIEWED);
        return toResponse(guideline);
    }

    @Transactional
    public GuidelineResponse approveGuideline(Long id, ApproveGuidelineRequest request) {
        Guideline guideline = getGuidelineEntity(id);
        if (guideline.getStatus() != GuidelineStatus.REVIEWED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only reviewed guideline can be approved");
        }
        if (request != null && request.teacherReviewNote() != null) {
            guideline.setTeacherReviewNote(request.teacherReviewNote());
        }
        guideline.setStatus(GuidelineStatus.APPROVED);
        return toResponse(guideline);
    }

    @Transactional
    public GuidelineResponse shareGuideline(Long id, ShareGuidelineRequest request) {
        Guideline guideline = getGuidelineEntity(id);
        if (guideline.getStatus() != GuidelineStatus.APPROVED) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Only approved guideline can be shared");
        }
        GuidelineAudience audience = resolveShareAudience(request);
        if (audience == GuidelineAudience.TEACHER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Share audience must be PARENT or SHARED");
        }
        if (request != null && request.teacherReviewNote() != null) {
            guideline.setTeacherReviewNote(request.teacherReviewNote());
        }
        guideline.setAudience(audience);
        guideline.setStatus(GuidelineStatus.SHARED);
        return toResponse(guideline);
    }

    private GuidelineAudience resolveShareAudience(ShareGuidelineRequest request) {
        if (request == null || request.audience() == null) {
            return GuidelineAudience.SHARED;
        }
        return request.audience();
    }

    private Guideline getGuidelineEntity(Long id) {
        return guidelineRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Guideline not found"));
    }

    private GuidelineResponse toResponse(Guideline guideline) {
        Long feedbackReportId = feedbackReportRepository.findByAiRequestId(guideline.getAiRequest().getId())
                .map(FeedbackReport::getId)
                .orElse(null);

        return new GuidelineResponse(
                guideline.getId(),
                guideline.getAiRequest().getId(),
                guideline.getSession().getId(),
                guideline.getStudent().getId(),
                guideline.getPriorityGap(),
                guideline.getSummary(),
                guideline.getStrength(),
                guideline.getImprovement(),
                guideline.getNextStep(),
                guideline.getSchoolAction(),
                guideline.getHomeAction(),
                guideline.getNextCheck(),
                guideline.getAudience(),
                guideline.getStatus(),
                guideline.getTeacherReviewNote(),
                feedbackReportId,
                guideline.getCreatedAt(),
                guideline.getUpdatedAt()
        );
    }
}
