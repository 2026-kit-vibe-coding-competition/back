package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.request.CreateAiRequestRequest;
import hackerton.educationentity.application.dto.response.AiRequestResponse;
import hackerton.educationentity.application.service.AiRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/ai-requests")
public class AiRequestController {
    private final AiRequestService aiRequestService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public AiRequestResponse createAiRequest(@RequestBody CreateAiRequestRequest request) {
        return aiRequestService.createAiRequest(request);
    }

    @GetMapping("/{id}")
    public AiRequestResponse getAiRequest(@PathVariable Long id) {
        return aiRequestService.getAiRequest(id);
    }
}
