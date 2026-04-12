package hackerton.educationentity.application.service;

import hackerton.educationentity.domain.evaluation.entity.Evaluation;
import hackerton.educationentity.domain.evaluation.type.EvaluationStatus;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EvaluationStatusService {

    public void applyStatus(Evaluation evaluation, EvaluationStatus status, LocalDateTime analyzingExpireTime) {
        evaluation.setStatus(status);
        evaluation.setAnalyzingExpireTime(resolveExpireTime(status, analyzingExpireTime));
    }

    public EvaluationStatus resolveStatus(EvaluationStatus requestedStatus) {
        return requestedStatus == null ? EvaluationStatus.ANALYZING : requestedStatus;
    }

    private LocalDateTime resolveExpireTime(EvaluationStatus status, LocalDateTime requestedExpireTime) {
        if (status == EvaluationStatus.ANALYZING) {
            return requestedExpireTime != null ? requestedExpireTime : LocalDateTime.now().plusMinutes(30);
        }
        if (status == EvaluationStatus.DELETED) {
            return null;
        }
        return requestedExpireTime;
    }
}
