package com.example.automating_text.data;

import java.util.Objects;

public class TextData {
    private String id;
    private String content;

    public TextData(String id, String content) {
        this.id = id;
        this.content = content;
    }

    // Getters and setters
    public String getId() { return id; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TextData textData = (TextData) o;
        return Objects.equals(id, textData.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}