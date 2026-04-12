package hackerton.educationentity.application.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

public class AiDto {

    @Getter
    @NoArgsConstructor
    public static class AssignmentFeedbackRequest {
        @NotBlank(message = "과제 내용은 필수입니다.")
        private String assignmentText;

        private String rubricText;
        private String answerGuideText;
    }

    @Getter
    @RequiredArgsConstructor
    public static class AssignmentFeedbackResponse {
        private final String result;
    }

    @Getter
    @NoArgsConstructor
    public static class GuidelineRequest {
        @NotBlank(message = "학생 데이터는 필수입니다.")
        private String studentDataJson;
    }

    @Getter
    @RequiredArgsConstructor
    public static class GuidelineResponse {
        private final String result;
    }

    @Getter
    @RequiredArgsConstructor
    public static class OcrPreviewResponse {
        private final String extractedText;
    }

    @Getter
    @RequiredArgsConstructor
    public static class AssignmentFeedbackWithOcrResponse {
        private final String extractedText;
        private final String result;
    }
}