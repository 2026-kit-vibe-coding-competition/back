package hackerton.educationentity.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AiChatRequest {

    @NotBlank(message = "질문은 필수입니다.")
    private String message;
}

