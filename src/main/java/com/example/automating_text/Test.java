package com.example.automating_text;

import com.example.automating_text.analysis.TextAnalyzer;
import com.example.automating_text.file.FileProcessor;
import com.example.automating_text.regex.RegexProcessor;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class Test {
    public static void main(String[] args) throws IOException {
        FileProcessor fileProcessor = new FileProcessor();
        RegexProcessor processor = new RegexProcessor();
        TextAnalyzer analyzer = new TextAnalyzer();

        String filePath = "src/main/resources/data/text";

        String email = "[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}";

        String text = "Too good bboaduboateng200@gmail.com to be true b@gmail.com";

        fileProcessor.writeFile(filePath, text);

        String reader = fileProcessor.readFile(filePath);

        List<String> foundEmails = processor.searchMatches(email, reader);

        String updatedMatches = processor.replaceMatches("[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,6}", reader, "bboaduboateng2000@gmail.com");

        fileProcessor.writeFile(filePath, updatedMatches);

        reader = fileProcessor.readFile(filePath);

        String finalContent = fileProcessor.readFile(filePath);

        Map<String, Long> wordCount = analyzer.numberOfWords(finalContent);
        System.out.println("\nWord Frequency Analysis:");
        for (Map.Entry<String, Long> entry : wordCount.entrySet()) {
            System.out.println(entry.getKey() + " : " + entry.getValue());
        }

        long numberOfLines = analyzer.countLines(finalContent);
        System.out.println("\nNumber of Lines: " + numberOfLines);

        System.out.println(foundEmails);
        System.out.print(reader);
    }
}
