package hackerton.educationentity.domain.guideline.repository;

import hackerton.educationentity.domain.guideline.entity.Guideline;
import hackerton.educationentity.domain.guideline.type.GuidelineAudience;
import hackerton.educationentity.domain.guideline.type.GuidelineStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GuidelineRepository extends JpaRepository<Guideline, Long> {
    Optional<Guideline> findByAiRequestId(Long aiRequestId);

    List<Guideline> findByAudience(GuidelineAudience audience);

    List<Guideline> findByStudentIdInAndStatusInOrderByUpdatedAtDesc(List<Long> studentIds, List<GuidelineStatus> statuses);
}
