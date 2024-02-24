package me.diogo.openaijava.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.Stream;

@Getter
@ToString
@AllArgsConstructor
public enum OpenAiModel {
    GPT_4("gpt-4", 8192, LocalDate.of(2021, 9, 1), new BigDecimal("0.03"), new BigDecimal("0.06")),
    GPT_4_0125_PREVIEW("gpt-4-0125-preview", 128000, LocalDate.of(2023, 12, 1), new BigDecimal("0.01"), new BigDecimal("0.03")),
    GPT_35_TURBO("gpt-3.5-turbo", 4096, LocalDate.of(2021, 9, 1), new BigDecimal("0.0030"), new BigDecimal("0.0060")),
    GPT_35_TURBO_0125("gpt-3.5-turbo-0125", 16385, LocalDate.of(2021, 9, 1), new BigDecimal("0.0005"), new BigDecimal("0.0015"));

    private final String name;
    private final int tokens;
    private final LocalDate trainingData;
    private final BigDecimal inputPrice;
    private final BigDecimal outputPrice;


    public static OpenAiModel mostUpdated() {
        return Stream.of(OpenAiModel.values())
                .max(Comparator.comparing(OpenAiModel::getTrainingData))
                .orElse(null);
    }

    public static OpenAiModel mostTokens() {
        return Stream.of(OpenAiModel.values())
                .max(Comparator.comparingInt(OpenAiModel::getTokens))
                .orElse(null);
    }

    public static OpenAiModel lowestPrice() {
        return Stream.of(OpenAiModel.values())
                .min(Comparator.comparing(OpenAiModel::getInputPrice)
                        .thenComparing(OpenAiModel::getOutputPrice))
                .orElse(null);
    }

    public static OpenAiModel getDefault() {
        return lowestPrice();
    }

}
