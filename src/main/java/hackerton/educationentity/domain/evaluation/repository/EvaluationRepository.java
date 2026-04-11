package hackerton.educationentity.domain.evaluation.repository;

import hackerton.educationentity.domain.evaluation.entity.Evaluation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EvaluationRepository extends JpaRepository<Evaluation, Long> {
    @Query("""
            select e from Evaluation e
            where (:sessionId is null or e.session.id = :sessionId)
              and (:studentId is null or e.student.id = :studentId)
            """)
    List<Evaluation> search(@Param("sessionId") Long sessionId,
                            @Param("studentId") Long studentId);

    List<Evaluation> findBySessionId(Long sessionId);

    List<Evaluation> findByStudentId(Long studentId);

    boolean existsBySessionIdAndStudentId(Long sessionId, Long studentId);

    boolean existsBySessionIdAndStudentIdAndIdNot(Long sessionId, Long studentId, Long id);
}
