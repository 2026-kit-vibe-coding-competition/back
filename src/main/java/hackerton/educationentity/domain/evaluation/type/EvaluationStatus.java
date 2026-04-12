package hackerton.educationentity.domain.evaluation.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum EvaluationStatus {
    ANALYZING("analyzing"),
    COMPLETED("completed"),
    FAILED("failed"),
    DELETED("deleted");

    private final String value;

    EvaluationStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static EvaluationStatus from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return Arrays.stream(values())
                .filter(status -> status.value.equalsIgnoreCase(value) || status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown evaluation status: " + value));
    }
}
