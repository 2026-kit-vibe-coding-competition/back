package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.request.ApproveGuidelineRequest;
import hackerton.educationentity.application.dto.request.ReviewGuidelineRequest;
import hackerton.educationentity.application.dto.request.ShareGuidelineRequest;
import hackerton.educationentity.application.dto.response.GuidelineResponse;
import hackerton.educationentity.application.service.GuidelineService;
import hackerton.educationentity.domain.guideline.type.GuidelineAudience;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/guidelines")
public class GuidelineController {
    private final GuidelineService guidelineService;

    @GetMapping("/{id}")
    public GuidelineResponse getGuideline(@PathVariable Long id) {
        return guidelineService.getGuideline(id);
    }

    @GetMapping
    public List<GuidelineResponse> getGuidelinesByAudience(@RequestParam(required = false) GuidelineAudience audience) {
        return guidelineService.getGuidelinesByAudience(audience == null ? GuidelineAudience.TEACHER : audience);
    }

    @PostMapping("/{id}/review")
    public GuidelineResponse reviewGuideline(@PathVariable Long id, @RequestBody ReviewGuidelineRequest request) {
        return guidelineService.reviewGuideline(id, request);
    }

    @PostMapping("/{id}/approve")
    public GuidelineResponse approveGuideline(@PathVariable Long id, @RequestBody ApproveGuidelineRequest request) {
        return guidelineService.approveGuideline(id, request);
    }

    @PostMapping("/{id}/share")
    public GuidelineResponse shareGuideline(@PathVariable Long id, @RequestBody ShareGuidelineRequest request) {
        return guidelineService.shareGuideline(id, request);
    }
}
