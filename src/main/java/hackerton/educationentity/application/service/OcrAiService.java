package hackerton.educationentity.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class OcrAiService {

    private final RestClient ollamaRestClient;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${app.ai.ocr-model}")
    private String ocrModel;

    public String extractText(MultipartFile file) {
        validateImageFile(file);

        String base64Image = encodeBase64(file);
        String responseBody = requestOcr(base64Image);

        return extractContent(responseBody);
    }

    private void validateImageFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("업로드된 파일이 없습니다.");
        }

        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("지금 단계에서는 이미지 파일만 업로드할 수 있습니다. png, jpg, jpeg 파일을 사용하세요.");
        }
    }

    private String encodeBase64(MultipartFile file) {
        try {
            return Base64.getEncoder().encodeToString(file.getBytes());
        } catch (IOException e) {
            throw new IllegalStateException("이미지 파일을 읽는 중 오류가 발생했습니다.", e);
        }
    }

    private String requestOcr(String base64Image) {
        Map<String, Object> requestBody = Map.of(
                "model", ocrModel,
                "prompt", """
                        이 이미지에 있는 텍스트를 최대한 원문 그대로 추출해줘.
                        설명하지 말고, 번역하지 말고, 보이는 텍스트만 출력해.
                        줄바꿈도 가능한 한 유지해.
                        """,
                "images", List.of(base64Image),
                "stream", false
        );

        String responseBody = ollamaRestClient.post()
                .uri("/api/generate")
                .contentType(MediaType.APPLICATION_JSON)
                .body(requestBody)
                .retrieve()
                .body(String.class);

        if (responseBody == null || responseBody.isBlank()) {
            throw new IllegalStateException("OCR 응답이 비어 있습니다.");
        }

        return responseBody;
    }

    private String extractContent(String responseBody) {
        log.info("{}", responseBody);
        try {
            JsonNode root = objectMapper.readTree(responseBody);
            String content = root.path("response").asText();

            if (content == null || content.isBlank()) {
                throw new IllegalStateException("OCR 결과 텍스트가 비어 있습니다.");
            }

            return normalize(content);
        } catch (IOException e) {
            throw new IllegalStateException("OCR 응답 파싱 중 오류가 발생했습니다.", e);
        }
    }

    private String normalize(String text) {
        return text
                .replace("\r", "")
                .replaceAll("\\n{3,}", "\n\n")
                .trim();
    }
}