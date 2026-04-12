package hackerton.educationentity.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.Resource;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.util.Iterator;
import java.util.Set;

@Service
public class GuidelineAiService {

    private static final Set<String> ALLOWED_RISK_LEVELS = Set.of("low", "medium", "high");
    private static final Set<String> ALLOWED_PRIORITY_GAPS = Set.of(
            "핵심 개념", "문제 적용", "과제 수행", "정확도", "참여도"
    );
    private static final Set<String> ALLOWED_TREND_DIRECTIONS = Set.of("up", "flat", "down");
    private static final Set<String> ALLOWED_SCHOOL_ACTION_CATEGORIES = Set.of(
            "개념 재설명", "시각화 자료", "단계별 질문", "추가 확인 문제", "짝 설명 활동", "심화 문제"
    );
    private static final Set<String> ALLOWED_HOME_ACTION_CATEGORIES = Set.of(
            "풀이 말로 설명", "짧은 반복 문제", "오답 다시 보기", "계산 연습", "학습 습관 점검"
    );

    @Resource(name = "guidelineChatClient")
    private ChatClient guidelineChatClient;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String generateGuideline(String studentDataJson) {
        String result = guidelineChatClient.prompt()
                .user("""
                        아래 학생 데이터를 분석해서 지정된 JSON 스키마 그대로만 출력하라.

                        학생 데이터:
                        %s
                        """.formatted(studentDataJson))
                .call()
                .content();

        validateResponse(result);
        return result;
    }

    private void validateResponse(String answer) {
        if (answer == null || answer.isBlank()) {
            throw new IllegalStateException("가이드라인 AI 응답이 비어 있습니다.");
        }

        JsonNode root = parseJson(answer);
        validateFieldExists(root, "risk_level");
        validateFieldExists(root, "priority_gap");
        validateFieldExists(root, "trend_direction");
        validateFieldExists(root, "primary_strength");
        validateFieldExists(root, "weakness_tags_selected");
        validateFieldExists(root, "school_action_category");
        validateFieldExists(root, "home_action_category");
        validateFieldExists(root, "evidence");

        validateValue(root.path("risk_level").asText(), ALLOWED_RISK_LEVELS, "risk_level");
        validateValue(root.path("priority_gap").asText(), ALLOWED_PRIORITY_GAPS, "priority_gap");
        validateValue(root.path("trend_direction").asText(), ALLOWED_TREND_DIRECTIONS, "trend_direction");

        validateArray(root.path("school_action_category"), ALLOWED_SCHOOL_ACTION_CATEGORIES, "school_action_category");
        validateArray(root.path("home_action_category"), ALLOWED_HOME_ACTION_CATEGORIES, "home_action_category");

        JsonNode evidence = root.path("evidence");
        if (!evidence.isArray()) {
            throw new IllegalStateException("evidence는 배열이어야 합니다.");
        }
        if (evidence.size() > 3) {
            throw new IllegalStateException("evidence는 최대 3개까지만 허용됩니다.");
        }
    }

    private JsonNode parseJson(String answer) {
        try {
            return objectMapper.readTree(answer);
        } catch (JsonProcessingException e) {
            throw new IllegalStateException("가이드라인 AI 응답이 JSON 형식이 아닙니다. 응답: " + answer, e);
        }
    }

    private void validateFieldExists(JsonNode root, String fieldName) {
        if (!root.has(fieldName)) {
            throw new IllegalStateException("필수 필드가 없습니다: " + fieldName);
        }
    }

    private void validateValue(String value, Set<String> allowed, String fieldName) {
        if (!allowed.contains(value)) {
            throw new IllegalStateException("허용되지 않은 " + fieldName + " 입니다: " + value);
        }
    }

    private void validateArray(JsonNode node, Set<String> allowed, String fieldName) {
        if (!node.isArray()) {
            throw new IllegalStateException(fieldName + "는 배열이어야 합니다.");
        }

        Iterator<JsonNode> iterator = node.iterator();
        while (iterator.hasNext()) {
            String value = iterator.next().asText();
            if (!allowed.contains(value)) {
                throw new IllegalStateException("허용되지 않은 " + fieldName + " 입니다: " + value);
            }
        }
    }
}