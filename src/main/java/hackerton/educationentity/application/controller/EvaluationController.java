package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.request.CreateEvaluationRequest;
import hackerton.educationentity.application.dto.request.UpdateEvaluationRequest;
import hackerton.educationentity.application.dto.response.EvaluationResponse;
import hackerton.educationentity.application.service.EvaluationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/evaluations")
public class EvaluationController {
    private final EvaluationService evaluationService;

    @GetMapping
    public List<EvaluationResponse> getEvaluations(@RequestParam(required = false) Long sessionId,
                                                   @RequestParam(required = false) Long studentId) {
        return evaluationService.getEvaluations(sessionId, studentId);
    }

    @GetMapping("/{id}")
    public EvaluationResponse getEvaluation(@PathVariable Long id) {
        return evaluationService.getEvaluation(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EvaluationResponse createEvaluation(@RequestBody CreateEvaluationRequest request) {
        return evaluationService.createEvaluation(request);
    }

    @PutMapping("/{id}")
    public EvaluationResponse updateEvaluation(@PathVariable Long id, @RequestBody UpdateEvaluationRequest request) {
        return evaluationService.updateEvaluation(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEvaluation(@PathVariable Long id) {
        evaluationService.deleteEvaluation(id);
    }
}
