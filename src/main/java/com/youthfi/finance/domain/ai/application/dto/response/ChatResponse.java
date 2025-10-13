package com.youthfi.finance.domain.ai.application.dto.response;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ChatResponse(
    @JsonProperty("reply_text")
    String replyText,
    
    @JsonProperty("action_type")
    String actionType,
    
    @JsonProperty("action_data")
    Map<String, Object> actionData,
    
    @JsonProperty("chart_image")
    String chartImage,
    
    @JsonProperty("pinecone_results")
    List<Map<String, Object>> pineconeResults,
    
    Boolean success,
    String errorMessage
) {
    public static ChatResponse success(String replyText, String actionType, 
                                    Map<String, Object> actionData, String chartImage,
                                    List<Map<String, Object>> pineconeResults) {
        return new ChatResponse(
                replyText,
                actionType,
                actionData,
                chartImage,
                pineconeResults,
                true,
                null
        );
    }

    public static ChatResponse error(String errorMessage) {
        return new ChatResponse(
                errorMessage,
                "display_info",
                Map.of("error", true),
                null,
                null,
                false,
                errorMessage
        );
    }
}
