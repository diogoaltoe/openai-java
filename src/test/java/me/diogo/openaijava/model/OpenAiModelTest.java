package me.diogo.openaijava.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OpenAiModelTest {
    @Test
    void success_to_get_most_updated_model() {
        Assertions.assertEquals(OpenAiModel.GPT_4_0125_PREVIEW, OpenAiModel.mostUpdated());
    }

    @Test
    void success_to_get_most_tokens_model() {
        Assertions.assertEquals(OpenAiModel.GPT_4_0125_PREVIEW, OpenAiModel.mostTokens());
    }
    @Test
    void success_to_get_lowest_price_model() {
        Assertions.assertEquals(OpenAiModel.GPT_35_TURBO_0125, OpenAiModel.lowestPrice());
    }

    @Test
    void success_to_get_default_model() {
        Assertions.assertEquals(OpenAiModel.GPT_35_TURBO_0125, OpenAiModel.getDefault());
    }

}