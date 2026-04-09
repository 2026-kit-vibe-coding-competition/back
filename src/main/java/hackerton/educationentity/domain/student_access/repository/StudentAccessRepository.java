package hackerton.educationentity.domain.student_access.repository;

import hackerton.educationentity.domain.student_access.entity.StudentAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentAccessRepository extends JpaRepository<StudentAccess, Long> {
}
