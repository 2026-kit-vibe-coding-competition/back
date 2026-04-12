package hackerton.educationentity.application.controller;

import hackerton.educationentity.application.dto.AiDto;
import hackerton.educationentity.application.service.AssignmentFeedbackAiService;
import hackerton.educationentity.application.service.GuidelineAiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import hackerton.educationentity.application.service.OcrAiService;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/ai")
@RequiredArgsConstructor
public class AiController {

    private final OcrAiService ocrAiService;
    private final AssignmentFeedbackAiService assignmentFeedbackAiService;
    private final GuidelineAiService guidelineAiService;

    @PostMapping("/assignment-feedback")
    public ResponseEntity<AiDto.AssignmentFeedbackResponse> generateAssignmentFeedback(
            @Valid @RequestBody AiDto.AssignmentFeedbackRequest request
    ) {
        String result = assignmentFeedbackAiService.generateFeedback(
                request.getAssignmentText(),
                request.getRubricText(),
                request.getAnswerGuideText()
        );

        return ResponseEntity.ok(new AiDto.AssignmentFeedbackResponse(result));
    }

    @PostMapping("/guideline")
    public ResponseEntity<AiDto.GuidelineResponse> generateGuideline(
            @Valid @RequestBody AiDto.GuidelineRequest request
    ) {
        String result = guidelineAiService.generateGuideline(request.getStudentDataJson());
        return ResponseEntity.ok(new AiDto.GuidelineResponse(result));
    }

    @PostMapping(value = "/ocr-preview", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AiDto.OcrPreviewResponse> previewOcr(
            @RequestPart("file") MultipartFile file
    ) {
        String extractedText = ocrAiService.extractText(file);
        return ResponseEntity.ok(new AiDto.OcrPreviewResponse(extractedText));
    }

    @PostMapping(value = "/assignment-feedback/ocr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<AiDto.AssignmentFeedbackWithOcrResponse> generateAssignmentFeedbackWithOcr(
            @RequestPart("file") MultipartFile file,
            @RequestParam(value = "rubricText", required = false) String rubricText,
            @RequestParam(value = "answerGuideText", required = false) String answerGuideText
    ) {
        String extractedText = ocrAiService.extractText(file);

        String result = assignmentFeedbackAiService.generateFeedback(
                extractedText,
                rubricText,
                answerGuideText
        );

        return ResponseEntity.ok(
                new AiDto.AssignmentFeedbackWithOcrResponse(extractedText, result)
        );
    }
}