package hackerton.educationentity.domain.guideline.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum GuidelineStatus {
    DRAFT("draft"),
    REVIEWED("reviewed"),
    APPROVED("approved"),
    SHARED("shared");

    private final String value;

    GuidelineStatus(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static GuidelineStatus from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return Arrays.stream(values())
                .filter(status -> status.value.equalsIgnoreCase(value) || status.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown guideline status: " + value));
    }
}
