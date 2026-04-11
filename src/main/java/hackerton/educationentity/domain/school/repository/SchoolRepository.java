package hackerton.educationentity.domain.school.repository;

import hackerton.educationentity.domain.school.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRepository extends JpaRepository<School, Long> {
    @Query("""
            select s from School s
            where (:name is null or lower(s.name) like lower(concat('%', :name, '%')))
              and (:type is null or s.type = :type)
            """)
    List<School> search(@Param("name") String name,
                        @Param("type") String type);
}
