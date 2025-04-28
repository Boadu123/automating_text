package com.example.automating_text.regex;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexProcessor {

//    Search for matches in an input text
    public List<String> searchMatches(String pattern, String inputText){
        List<String> matches = new ArrayList<>();

        Pattern compiledPattern = Pattern.compile(pattern);

        Matcher matcher = compiledPattern.matcher(inputText);

        while(matcher.find()){
            matches.add(matcher.group());
        }
        return matches;
    }

//    replace matched pattern with a specific input
    public String replaceMatches(String pattern, String inputText, String replacement){
        Pattern compiledPattern = Pattern.compile(pattern);

        Matcher matches = compiledPattern.matcher(inputText);

        return matches.replaceAll(replacement);
    }
}
