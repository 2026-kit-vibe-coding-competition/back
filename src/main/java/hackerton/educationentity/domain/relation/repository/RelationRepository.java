package hackerton.educationentity.domain.relation.repository;

import hackerton.educationentity.domain.relation.entity.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long> {
    @Query("""
            select r from Relation r
            where (:parentId is null or r.parent.id = :parentId)
              and (:studentId is null or r.student.id = :studentId)
              and (:relation is null or lower(r.relation) like lower(concat('%', cast(:relation as string), '%')))
            """)
    List<Relation> search(@Param("parentId") Long parentId,
                          @Param("studentId") Long studentId,
                          @Param("relation") String relation);

    List<Relation> findByParentId(Long parentId);

    boolean existsByParentIdAndStudentId(Long parentId, Long studentId);

    boolean existsByParentIdAndStudentIdAndIdNot(Long parentId, Long studentId, Long id);
}
