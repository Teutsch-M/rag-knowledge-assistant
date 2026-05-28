package com.teutsch.ragassistant.dto;

import java.util.List;

public class ChatResponse {

    private String answer;
    private List<SourceDto> sources;

    public ChatResponse(String answer, List<SourceDto> sources) {
        this.answer = answer;
        this.sources = sources;
    }

    public String getAnswer() {
        return answer;
    }

    public List<SourceDto> getSources() {
        return sources;
    }
}
