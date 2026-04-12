package hackerton.educationentity.domain.user.repository;

import hackerton.educationentity.domain.user.entity.User;
import hackerton.educationentity.domain.user.entity.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    boolean existsByEmailAndIdNot(String email, Long id);

    @Query("""
            select u from User u
            where (:schoolId is null or u.school.id = :schoolId)
              and (:role is null or u.role = :role)
              and (:name is null or lower(u.name) like lower(concat('%', cast(:name as string), '%')))
              and (:email is null or lower(u.email) like lower(concat('%', cast(:email as string), '%')))
            """)
    List<User> search(@Param("schoolId") Long schoolId,
                      @Param("role") UserRole role,
                      @Param("name") String name,
                      @Param("email") String email);
}
