package hackerton.educationentity.domain.relation.repository;

import hackerton.educationentity.domain.relation.entity.Relation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RelationRepository extends JpaRepository<Relation, Long> {
}
