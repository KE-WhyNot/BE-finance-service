package com.youthfi.finance.domain.ai.application.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatResponse(
    @JsonProperty("reply_text")
    String replyText,
    
    @JsonProperty("action_type")
    String actionType,
    
    @JsonProperty("action_data")
    ActionData actionData,
    
    @JsonProperty("chart_image")
    String chartImage,
    
    @JsonProperty("success")
    Boolean success,
    
    @JsonProperty("error_message")
    String errorMessage
) {
    public static ChatResponse success(String replyText, String actionType, 
                                    ActionData actionData, String chartImage) {
        return new ChatResponse(
                replyText,
                actionType,
                actionData,
                chartImage,
                true,
                null
        );
    }

    public static ChatResponse error(String errorMessage) {
        return new ChatResponse(
                errorMessage,
                "display_info",
                null,
                null,
                false,
                errorMessage
        );
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ActionData(
        @JsonProperty("query_analysis")
        QueryAnalysis queryAnalysis,

        @JsonProperty("confidence_evaluation")
        ConfidenceEvaluation confidenceEvaluation
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record QueryAnalysis(
        @JsonProperty("primary_intent")
        String primaryIntent,

        @JsonProperty("complexity_level")
        String complexityLevel
    ) {}

    @JsonIgnoreProperties(ignoreUnknown = true)
    public record ConfidenceEvaluation(
        @JsonProperty("overall_confidence")
        Double overallConfidence
    ) {}
}
