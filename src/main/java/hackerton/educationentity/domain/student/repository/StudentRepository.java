package hackerton.educationentity.domain.student.repository;

import hackerton.educationentity.domain.student.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {
    @Query("""
            select s from Student s
            where (:classroomId is null or s.classroom.id = :classroomId)
              and (:status is null or s.status = :status)
              and (:name is null or lower(s.name) like lower(concat('%', cast(:name as string), '%')))
            """)
    List<Student> search(@Param("classroomId") Long classroomId,
                         @Param("name") String name,
                         @Param("status") String status);
}
