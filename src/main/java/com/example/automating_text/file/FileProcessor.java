package com.example.automating_text.file;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileProcessor {

//    read from file using BufferedReader
    public String readFile(String filepath) throws IOException {

        if(filepath == null || filepath.trim().isEmpty()){
            throw new IllegalArgumentException("The file path is Empty " + filepath);
        }

        Path path = Paths.get(filepath);
        StringBuilder content = new StringBuilder();

        try (BufferedReader reader = Files.newBufferedReader(path)){
            String line;
            while((line = reader.readLine()) != null){
                content.append(line).append(System.lineSeparator());
            }
        }catch (NoSuchFileException e) {
            throw new IOException("File not found: " + filepath, e);
        }
        return content.toString();
    }

//    Write to a file using BufferedWriter
    public void writeFile(String filepath, String content) throws IOException{
        if(filepath == null || filepath.trim().isEmpty()){
            throw new IllegalArgumentException("File path cannot be empty or null");
        }

        if(content == null){
            throw new IllegalArgumentException("Content cannot be null ");
        }

        try(BufferedWriter writer = Files.newBufferedWriter(Paths.get(filepath))){
            writer.write(content);
        }
    }
}
