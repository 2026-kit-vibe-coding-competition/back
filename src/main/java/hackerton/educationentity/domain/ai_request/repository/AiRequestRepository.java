package hackerton.educationentity.domain.ai_request.repository;

import hackerton.educationentity.domain.ai_request.entity.AiRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AiRequestRepository extends JpaRepository<AiRequest, Long> {
    Optional<AiRequest> findByJobId(String jobId);
}
