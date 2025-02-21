package com.progressoft.FxDealsWarehouse.util;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class CsvFileReader implements FileReader {

    @Override
    public Set<String> read ( Path path ) {
        try {
            return Files.lines(path).skip(1).map(line -> {
                String[] parts = line.split(",");
                return parts[0].trim();
            }).collect(Collectors.toSet());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read currency codes file", e);
        }
    }
}
