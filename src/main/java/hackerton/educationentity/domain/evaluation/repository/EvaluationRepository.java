package hackerton.educationentity.domain.evaluation.repository;

import hackerton.educationentity.domain.evaluation.entity.Evaluation;
import hackerton.educationentity.domain.evaluation.type.EvaluationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    @Query("""
            select e from Evaluation e
            where (:sessionId is null or e.session.id = :sessionId)
              and (:studentId is null or e.student.id = :studentId)
              and e.status <> :deletedStatus
            """)
    List<Evaluation> search(@Param("sessionId") Long sessionId,
                            @Param("studentId") Long studentId,
                            @Param("deletedStatus") EvaluationStatus deletedStatus);

    List<Evaluation> findBySessionIdAndStatusNot(Long sessionId, EvaluationStatus status);

    List<Evaluation> findByStudentIdAndStatusNot(Long studentId, EvaluationStatus status);

    boolean existsBySessionIdAndStudentIdAndStatusNot(Long sessionId, Long studentId, EvaluationStatus status);

    boolean existsBySessionIdAndStudentIdAndIdNotAndStatusNot(Long sessionId, Long studentId, Long id, EvaluationStatus status);

    Optional<Evaluation> findByIdAndStatusNot(Long id, EvaluationStatus status);

    Optional<Evaluation> findBySessionIdAndStudentIdAndStatusNot(Long sessionId, Long studentId, EvaluationStatus status);
}
