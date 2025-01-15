package org.example.finalproject.iohandler;

import org.example.finalproject.main.exceptions.FileNameAlreadyExists;
import org.junit.jupiter.api.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArgumentParserTest {

    private final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private ArgumentParser parser;

    @BeforeEach
    void setUp() {
        System.setOut(new PrintStream(outputStream));
        parser = new ArgumentParser();
    }

    @AfterEach
    void tearDown() {
        System.setOut(originalOut);
        outputStream.reset();
    }

    @Test
    void testDisplayHelp() throws FileNameAlreadyExists {
        parser.parseArguments(List.of("--help"));
        String output = outputStream.toString();
        assertTrue(output.contains("Options:"));
        assertTrue(output.contains("-l <user/admin>"));
        assertTrue(output.contains("--help"));
    }


}
