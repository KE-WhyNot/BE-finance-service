package com.youthfi.finance.domain.ai.application.dto.request;

import java.util.List;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public record KnowledgeBaseUpdateRequest(
    @NotEmpty(message = "문서 목록은 필수입니다.")
    List<Document> documents
) {
    public record Document(
        @NotNull(message = "문서 ID는 필수입니다.")
        String documentId,
        
        @NotNull(message = "문서 내용은 필수입니다.")
        String content,
        
        String title,
        String category,
        String source
    ) {
    }
}
