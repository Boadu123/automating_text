package com.example.automating_text.data;

import java.util.Objects;
import java.util.UUID;

public class TextData {
    private String id;
    private String content;

    public TextData(String id, String content) {
        if (id == null || id.trim().isEmpty()) {
            throw new IllegalArgumentException("ID cannot be null or empty");
        }
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