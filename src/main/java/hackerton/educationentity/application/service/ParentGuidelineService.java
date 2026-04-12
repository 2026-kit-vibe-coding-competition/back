package hackerton.educationentity.application.service;

import hackerton.educationentity.application.dto.response.ParentLatestGuidelineResponse;
import hackerton.educationentity.domain.guideline.entity.Guideline;
import hackerton.educationentity.domain.guideline.repository.GuidelineRepository;
import hackerton.educationentity.domain.guideline.type.GuidelineStatus;
import hackerton.educationentity.domain.relation.repository.RelationRepository;
import hackerton.educationentity.domain.user.entity.UserRole;
import hackerton.educationentity.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ParentGuidelineService {
    private final UserRepository userRepository;
    private final RelationRepository relationRepository;
    private final GuidelineRepository guidelineRepository;

    public ParentLatestGuidelineResponse getLatestSharedGuideline(Long parentUserId) {
        var parent = userRepository.findById(parentUserId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User not found"));

        if (parent.getRole() != UserRole.PARENT) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Only parent can access this endpoint");
        }

        List<Long> studentIds = relationRepository.findByParentId(parentUserId).stream()
                .map(relation -> relation.getStudent().getId())
                .distinct()
                .toList();

        if (studentIds.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No related student found");
        }

        Guideline latestSharedGuideline = guidelineRepository
                .findByStudentIdInAndStatusInOrderByUpdatedAtDesc(studentIds, List.of(GuidelineStatus.SHARED))
                .stream()
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "No shared guideline found"));

        return new ParentLatestGuidelineResponse(
                latestSharedGuideline.getStudent().getId(),
                latestSharedGuideline.getStudent().getName(),
                latestSharedGuideline.getId(),
                latestSharedGuideline.getPriorityGap(),
                latestSharedGuideline.getSummary(),
                latestSharedGuideline.getStrength(),
                latestSharedGuideline.getImprovement(),
                latestSharedGuideline.getNextStep(),
                latestSharedGuideline.getSchoolAction(),
                latestSharedGuideline.getHomeAction(),
                latestSharedGuideline.getNextCheck(),
                latestSharedGuideline.getStatus(),
                latestSharedGuideline.getUpdatedAt()
        );
    }
}
