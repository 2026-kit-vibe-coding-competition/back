package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.response.ParentLatestGuidelineResponse;
import hackerton.educationentity.application.service.ParentGuidelineService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/parent")
public class ParentController {
    private final ParentGuidelineService parentGuidelineService;

    @GetMapping("/me/latest-guideline")
    public ParentLatestGuidelineResponse getLatestGuideline(HttpServletRequest request) {
        return parentGuidelineService.getLatestSharedGuideline(resolveAuthUserId(request));
    }

    private Long resolveAuthUserId(HttpServletRequest request) {
        Object authUserId = request.getAttribute("authUserId");
        if (authUserId == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        if (authUserId instanceof Number number) {
            return number.longValue();
        }
        try {
            return Long.parseLong(String.valueOf(authUserId));
        } catch (NumberFormatException ignored) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }
    }
}
