package com.example.automating_text.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileProcessor {

//    read from file using BufferedReader
    public String readFile(String filepath) throws IOException {

        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(filepath))){
            String line;
            while((line = reader.readLine()) != null){
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

//    Write to a file using BufferedWriter
    public void writeFile(String filepath, String content) throws IOException{
        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(filepath))){
            writer.write(content);
        }
    }
}
