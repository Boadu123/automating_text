package com.example.automating_text.regex;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.regex.PatternSyntaxException;

import static org.junit.jupiter.api.Assertions.*;

class RegexProcessorTest {

    private RegexProcessor regexProcessor;

    @BeforeEach
    void setUp() {
        regexProcessor = new RegexProcessor();
    }

    @Test
    void testSearchMatches_validPattern_returnsMatches() {
        String pattern = "\\b\\w{4}\\b"; // matches 4-letter words
        String inputText = "This test will pass well";
        List<String> matches = regexProcessor.searchMatches(pattern, inputText);
        assertEquals(List.of("This", "test", "will", "pass", "well"), matches);
    }

    @Test
    void testSearchMatches_noMatch_returnsEmptyList() {
        String pattern = "\\d+"; // match digits
        String inputText = "No digits here";
        List<String> matches = regexProcessor.searchMatches(pattern, inputText);
        assertTrue(matches.isEmpty());
    }

    @Test
    void testSearchMatches_invalidPattern_throwsException() {
        String pattern = "*invalid";
        String inputText = "Some text";
        assertThrows(PatternSyntaxException.class, () -> regexProcessor.searchMatches(pattern, inputText));
    }

    @Test
    void testReplaceMatches_validPattern_replacesCorrectly() {
        String pattern = "\\d+";
        String inputText = "My number is 12345 and yours is 67890";
        String replacement = "[number]";
        String result = regexProcessor.replaceMatches(pattern, inputText, replacement);
        assertEquals("My number is [number] and yours is [number]", result);
    }

    @Test
    void testReplaceMatches_noMatch_returnsOriginalText() {
        String pattern = "\\d+";
        String inputText = "No numbers here";
        String replacement = "[number]";
        String result = regexProcessor.replaceMatches(pattern, inputText, replacement);
        assertEquals(inputText, result);
    }

    @Test
    void testReplaceMatches_invalidPattern_throwsException() {
        String pattern = "*invalid";
        String inputText = "Text to replace";
        String replacement = "X";
        assertThrows(PatternSyntaxException.class, () -> regexProcessor.replaceMatches(pattern, inputText, replacement));
    }

    @Test
    void testSearchMatches_nullInputs_throwException() {
        assertThrows(IllegalArgumentException.class, () -> regexProcessor.searchMatches(null, "text"));
        assertThrows(IllegalArgumentException.class, () -> regexProcessor.searchMatches("", "text"));
        assertThrows(IllegalArgumentException.class, () -> regexProcessor.searchMatches("\\d+", null));
    }

    @Test
    void testReplaceMatches_nullInputs_throwException() {
        assertThrows(IllegalArgumentException.class, () -> regexProcessor.replaceMatches(null, "text", "X"));
        assertThrows(IllegalArgumentException.class, () -> regexProcessor.replaceMatches("", "text", "X"));
        assertThrows(IllegalArgumentException.class, () -> regexProcessor.replaceMatches("\\d+", null, "X"));
        assertThrows(IllegalArgumentException.class, () -> regexProcessor.replaceMatches("\\d+", "text", null));
    }
}
