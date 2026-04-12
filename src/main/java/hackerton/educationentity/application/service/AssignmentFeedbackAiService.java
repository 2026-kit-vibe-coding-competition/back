package hackerton.educationentity.application.service;

import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AssignmentFeedbackAiService {

    @Resource(name = "assignmentFeedbackChatClient")
    private ChatClient assignmentFeedbackChatClient;

    public String generateFeedback(String assignmentText, String rubricText, String answerGuideText) {
        String prompt = """
                아래 과제 내용을 읽고 학생 피드백을 작성하라.

                [과제 내용]
                %s

                [채점 기준]
                %s

                [정답 또는 해설 기준]
                %s
                """.formatted(
                nullSafe(assignmentText),
                nullSafe(rubricText),
                nullSafe(answerGuideText)
        );

        String result = assignmentFeedbackChatClient.prompt()
                .user(prompt)
                .call()
                .content();

        if (result == null || result.isBlank()) {
            throw new IllegalStateException("과제 피드백 AI 응답이 비어 있습니다.");
        }

        return result;
    }

    private String nullSafe(String value) {
        return value == null ? "" : value;
    }
}