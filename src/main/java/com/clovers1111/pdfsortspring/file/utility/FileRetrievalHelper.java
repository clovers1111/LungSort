package com.clovers1111.pdfsortspring.file.utility;

import com.clovers1111.pdfsortspring.file.FileTypes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class FileRetrievalHelper {

    protected FileRetrievalHelper() {
    }


    public static Path getRandomFile(final Path directory) throws IOException {
        return pickRandomFile(listRegularFiles(directory));
    }

    // Returns the file extension (with the dot included)
    public static String getFileExtension(final Path path) {
        final Path fileNamePath = path.getFileName();
        if (fileNamePath == null) {
            return "";
        }

        final String fileName = fileNamePath.toString();
        int lastDotIndex = fileName.lastIndexOf('.');
        return lastDotIndex >= 0 ? fileName.substring(lastDotIndex) : "";
    }

    // Non-directory files = not a directory, device file, etc.
    protected static List<Path> listRegularFiles(final Path directory) throws IOException {
        validateDirectory(directory);

        try (Stream<Path> paths = Files.list(directory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .toList();
        }
    }



    protected static Path pickRandomFile(final List<Path> files) {
        if (files.isEmpty()) {
            throw new IllegalStateException("No matching files found.");
        }

        final int randomIndex = ThreadLocalRandom.current().nextInt(files.size());
        return files.get(randomIndex);
    }

    protected static void validateDirectory(final Path directory) {
        Objects.requireNonNull(directory, "directory must not be null");

        if (!Files.exists(directory)) {
            throw new IllegalArgumentException("Directory does not exist: " + directory);
        }

        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("Path is not a directory: " + directory);
        }
    }
}
