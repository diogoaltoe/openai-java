package me.diogo.openaijava.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class OpenAiClientModelTest {
    @Test
    void successToGetMostUpdatedModel() {
        Assertions.assertEquals(OpenAiModel.GPT_4_0125_PREVIEW, OpenAiModel.mostUpdated());
    }

    @Test
    void successToGetMostTokensModel() {
        Assertions.assertEquals(OpenAiModel.GPT_4_0125_PREVIEW, OpenAiModel.mostTokens());
    }
    @Test
    void successToGetLowestPriceModel() {
        Assertions.assertEquals(OpenAiModel.GPT_35_TURBO_0125, OpenAiModel.lowestPrice());
    }

    @Test
    void successToGetDefaultModel() {
        Assertions.assertEquals(OpenAiModel.GPT_35_TURBO_0125, OpenAiModel.getDefault());
    }

}