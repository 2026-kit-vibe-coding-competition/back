package hackerton.educationentity.domain.guideline.type;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

import java.util.Arrays;

public enum GuidelineAudience {
    TEACHER("teacher"),
    PARENT("parent"),
    SHARED("shared");

    private final String value;

    GuidelineAudience(String value) {
        this.value = value;
    }

    @JsonValue
    public String value() {
        return value;
    }

    @JsonCreator
    public static GuidelineAudience from(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }

        return Arrays.stream(values())
                .filter(audience -> audience.value.equalsIgnoreCase(value) || audience.name().equalsIgnoreCase(value))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Unknown guideline audience: " + value));
    }
}
