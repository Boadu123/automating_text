package com.example.automating_text.analysis;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TextAnalyzerTest {

    private TextAnalyzer textAnalyzer;

    @BeforeEach
    void setUp() {
        textAnalyzer = new TextAnalyzer();
    }

    @Test
    void testCountWord_validInput_correctCount() {
        String content = "Hello world! Hello everyone. Hello there.";
        String target = "hello";
        Map<String, Long> result = textAnalyzer.countWord(content, target);
        assertEquals(3L, result.get(target));
    }

    @Test
    void testCountWord_caseInsensitiveAndPunctuation() {
        String content = "Test, test! TEST test?";
        String target = "test";
        Map<String, Long> result = textAnalyzer.countWord(content, target);
        assertEquals(4L, result.get(target));
    }

    @Test
    void testCountWord_noMatches_returnsZero() {
        String content = "This is a simple sentence.";
        String target = "hello";
        Map<String, Long> result = textAnalyzer.countWord(content, target);
        assertEquals(0L, result.get(target));
    }

    @Test
    void testCountWord_invalidInputs_throwException() {
        assertThrows(IllegalArgumentException.class, () -> textAnalyzer.countWord(null, "test"));
        assertThrows(IllegalArgumentException.class, () -> textAnalyzer.countWord("Some text", null));
        assertThrows(IllegalArgumentException.class, () -> textAnalyzer.countWord("Some text", ""));
    }

    @Test
    void testNumberOfWords_validInput_correctCounts() {
        String content = "Java is great. Java is robust.";
        Map<String, Long> wordCounts = textAnalyzer.numberOfWords(content);
        assertEquals(2L, wordCounts.get("java"));
        assertEquals(2L, wordCounts.get("is"));
        assertEquals(1L, wordCounts.get("great"));
        assertEquals(1L, wordCounts.get("robust"));
    }

    @Test
    void testNumberOfWords_ignoresPunctuationAndCase() {
        String content = "Hello! HELLO? hello.";
        Map<String, Long> wordCounts = textAnalyzer.numberOfWords(content);
        assertEquals(3L, wordCounts.get("hello"));
    }

    @Test
    void testNumberOfWords_emptyWordsIgnored() {
        String content = "   ,,, ,,,   ";
        Map<String, Long> wordCounts = textAnalyzer.numberOfWords(content);
        assertTrue(wordCounts.isEmpty());
    }

    @Test
    void testNumberOfWords_nullInput_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> textAnalyzer.numberOfWords(null));
    }

    @Test
    void testCountLines_validInput_returnsLineCount() {
        String content = "Line one\nLine two\nLine three";
        long lineCount = textAnalyzer.countLines(content);
        assertEquals(3L, lineCount);
    }

    @Test
    void testCountLines_emptyContent_returnsZero() {
        String content = "   ";
        long lineCount = textAnalyzer.countLines(content);
        assertEquals(0L, lineCount);
    }

    @Test
    void testCountLines_nullContent_returnsZero() {
        assertEquals(0L, textAnalyzer.countLines(null));
    }
}
