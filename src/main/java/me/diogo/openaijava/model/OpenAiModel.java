package me.diogo.openaijava.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OpenAiModel {
    GPT_4("gpt-4"),
    GPT_35_TURBO("gpt-3.5-turbo");

    private final String model;
}
