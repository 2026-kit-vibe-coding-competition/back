package hackerton.educationentity.domain.feedback_report.repository;

import hackerton.educationentity.domain.feedback_report.entity.FeedbackReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FeedbackReportRepository extends JpaRepository<FeedbackReport, Long> {
    Optional<FeedbackReport> findByAiRequestId(Long aiRequestId);

    @Query("""
            select fr from FeedbackReport fr
            where (:sessionId is null or fr.session.id = :sessionId)
              and (:studentId is null or fr.student.id = :studentId)
            """)
    List<FeedbackReport> search(@Param("sessionId") Long sessionId,
                                @Param("studentId") Long studentId);
}
