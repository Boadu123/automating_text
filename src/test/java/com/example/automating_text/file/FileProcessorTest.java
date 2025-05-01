package com.example.automating_text.file;

import org.junit.jupiter.api.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class FileProcessorTest {

    private FileProcessor fileProcessor;
    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        fileProcessor = new FileProcessor();
        tempFile = Files.createTempFile("test-file", ".txt");
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    @Test
    void testWriteFile_validInput_writesSuccessfully() throws IOException {
        String content = "Hello, this is a test.";
        fileProcessor.writeFile(tempFile.toString(), content);

        String writtenContent = Files.readString(tempFile);
        assertEquals(content, writtenContent);
    }

    @Test
    void testWriteFile_nullPath_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> fileProcessor.writeFile(null, "content"));
    }

    @Test
    void testWriteFile_emptyPath_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> fileProcessor.writeFile("   ", "content"));
    }

    @Test
    void testWriteFile_nullContent_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> fileProcessor.writeFile(tempFile.toString(), null));
    }

    @Test
    void testReadFile_validFile_readsSuccessfully() throws IOException {
        String content = "Line 1\nLine 2\nLine 3";
        Files.writeString(tempFile, content);

        String readContent = fileProcessor.readFile(tempFile.toString());
        // System.lineSeparator() is used by the processor
        String expected = String.join(System.lineSeparator(), "Line 1", "Line 2", "Line 3") + System.lineSeparator();
        assertEquals(expected, readContent);
    }

    @Test
    void testReadFile_fileNotFound_throwsIOException() {
        String fakePath = "nonexistent-file.txt";
        IOException exception = assertThrows(IOException.class,
                () -> fileProcessor.readFile(fakePath));
        assertTrue(exception.getMessage().contains("File not found"));
    }

    @Test
    void testReadFile_nullPath_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> fileProcessor.readFile(null));
    }

    @Test
    void testReadFile_emptyPath_throwsException() {
        assertThrows(IllegalArgumentException.class,
                () -> fileProcessor.readFile("  "));
    }
}
