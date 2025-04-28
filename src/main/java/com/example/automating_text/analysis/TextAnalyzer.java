package com.example.automating_text.analysis;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

public class TextAnalyzer {

//    remove spaces, remove punctuations, check for extra spaces and later count the words
    public Map<String, Long> numberOfWords(String content){
        return Arrays.stream(content.split("\\s+"))
                .map(word -> word.replaceAll("[^a-zAz]", ""))
                .filter(word -> !word.isEmpty())
                .collect(Collectors.groupingBy(word -> word, Collectors.counting()));
    }

//    Count lines
    public long countLines(String content) {
        return content.lines().count();
    }
}
