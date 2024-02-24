package me.diogo.openaijava.operation;

import com.knuddels.jtokkit.Encodings;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenCounter {
    public static int count(final String model, final String prompt) {
        final var registry = Encodings.newDefaultEncodingRegistry();
        final var encoding = registry.getEncodingForModel(model);
        if (encoding.isEmpty()) {
            log.warn("TokenCounter unable to get a encoding for the model: {}", model);
            return 0;
        }
        return encoding.get().countTokens(prompt);
    }
}
