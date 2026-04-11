package hackerton.educationentity.domain.student_access.repository;

import hackerton.educationentity.domain.student_access.entity.StudentAccess;
import hackerton.educationentity.domain.student_access.type.AccessType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentAccessRepository extends JpaRepository<StudentAccess, Long> {
    @Query("""
            select sa from StudentAccess sa
            where (:studentId is null or sa.student.id = :studentId)
              and (:teacherId is null or sa.teacher.id = :teacherId)
              and (:type is null or sa.type = :type)
            """)
    List<StudentAccess> search(@Param("studentId") Long studentId,
                               @Param("teacherId") Long teacherId,
                               @Param("type") AccessType type);

    boolean existsByStudentIdAndTeacherId(Long studentId, Long teacherId);

    boolean existsByStudentIdAndTeacherIdAndIdNot(Long studentId, Long teacherId, Long id);
}
