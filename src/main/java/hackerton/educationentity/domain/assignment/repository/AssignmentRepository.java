package hackerton.educationentity.domain.assignment.repository;

import hackerton.educationentity.domain.assignment.entity.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    @Query("""
            select a from Assignment a
            where (:sessionId is null or a.session.id = :sessionId)
              and (:title is null or lower(a.title) like lower(concat('%', cast(:title as string), '%')))
            """)
    List<Assignment> search(@Param("sessionId") Long sessionId,
                            @Param("title") String title);

    List<Assignment> findBySessionId(Long sessionId);
}
