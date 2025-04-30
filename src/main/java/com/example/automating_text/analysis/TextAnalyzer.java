package com.example.automating_text.analysis;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class TextAnalyzer {

//    check for occurrence of a word
    public Map<String, Long> countWord(String content, String targetedWord){
        if(content == null){
            throw new IllegalArgumentException("Content word cannot be null or empty ");
        }

        if (targetedWord == null || targetedWord.trim().isEmpty()) {
            throw new IllegalArgumentException("Target word cannot be null or empty");
        }

        long count = Arrays.stream(content.split("\\s+"))
                .map(word -> word.replaceAll("[^a-zA-Z]", "").toLowerCase())
                .filter(word -> word.equals(targetedWord.toLowerCase()))
                .count();
        return Map.of(targetedWord.toString(), count);
    }

//    count the number of times each word appears
    public Map<String, Long> numberOfWords(String content){
        if(content == null){
            throw new IllegalArgumentException("Content cannot be null or empty");
        }

        Map<String, Long> wordCounts = Arrays.stream(content.split("\\s+"))
                .map(word -> word.replaceAll("[^a-zA-Z]", "").toLowerCase())
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));

        return wordCounts;
    }

//    Count lines
    public long countLines(String content) {
        if (content == null || content.trim().isEmpty()) {
            return 0;
        }

        return content.lines().count();
    }
}
