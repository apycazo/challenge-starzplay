package com.github.apycazo.starzplay.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.apycazo.starzplay.data.MovieCatalog;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class TestUtils
{
    public static final String FILE_BASIC = "test-basic.json";
    public static final String UNCENSORED_MEDIA_GUID = "3-1-MFR";
    public static final String CENSORED_MEDIA_GUID = "3-2-C";

    private final ObjectMapper mapper = new ObjectMapper();

    private String readResourceAsString (String classpathFileName) throws IOException
    {
        Resource input = new ClassPathResource(classpathFileName);

        String text;
        try (Scanner scanner = new Scanner(input.getInputStream(), StandardCharsets.UTF_8.name())) {
            text = scanner.useDelimiter("\\A").next();
        }

        return text;
    }

    public MovieCatalog parseBasicFile () throws IOException
    {
        return parseFile(FILE_BASIC);
    }

    public MovieCatalog parseFile (String filename) throws IOException
    {
        String content = readResourceAsString(filename);
        return mapper.readValue(content, MovieCatalog.class);
    }
}
