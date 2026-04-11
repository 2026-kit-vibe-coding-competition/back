package hackerton.educationentity.domain.session.repository;

import hackerton.educationentity.domain.session.entity.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SessionRepository extends JpaRepository<Session, Long> {
    @Query("""
            select s from Session s
            where (:subjectId is null or s.subject.id = :subjectId)
              and (:date is null or s.date = :date)
              and (:title is null or lower(s.title) like lower(concat('%', :title, '%')))
            """)
    List<Session> search(@Param("subjectId") Long subjectId,
                         @Param("title") String title,
                         @Param("date") String date);
}
