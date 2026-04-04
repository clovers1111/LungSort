package com.clovers1111.pdfsortspring.file.utility;

import com.clovers1111.pdfsortspring.file.FileTypes;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public final class FileRetrievalService {

    private FileRetrievalService() {
    }

    public static Path getRandomFile(final Path directory) throws IOException {
        return getRandomFile(directory, null);
    }

    public static Path getRandomFile(final Path directory, final FileTypes type) throws IOException {
        if (type == null) {
            return pickRandomFile(listRegularFiles(directory));
        }

        return switch (type) {
            case JPG, PNG, JPEG -> getRandomImageFile(directory);
            case PDF -> getRandomPdfFile(directory);
        };
    }

    public static Path getRandomImageFile(final Path directory) throws IOException {
        return pickRandomFile(listRegularFiles(directory).stream()
                .filter(FileRetrievalService::isImageFile)
                .toList());
    }

    public static Path getRandomPdfFile(final Path directory) throws IOException {
        return pickRandomFile(listRegularFiles(directory).stream()
                .filter(path -> FileTypes.fromExtension(getFileExtension(path))
                        .filter(FileTypes.PDF::equals)
                        .isPresent())
                .toList());
    }

    // Non-directory files = not a directory, device file, etc.
    private static List<Path> listRegularFiles(final Path directory) throws IOException {
        validateDirectory(directory);

        try (Stream<Path> paths = Files.list(directory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .toList();
        }
    }

    public static List<Path> listImageFiles(final Path directory) {
        validateDirectory(directory);

        try (Stream<Path> paths = Files.list(directory)) {
            return paths
                    .filter(Files::isRegularFile)
                    .filter(path -> isImageFile(path))
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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

    private static boolean isImageFile(final Path path) {
        return FileTypes.fromExtension(getFileExtension(path))
                .map(FileTypes::isImage)
                .orElse(false);
    }

    private static Path pickRandomFile(final List<Path> files) {
        if (files.isEmpty()) {
            throw new IllegalStateException("No matching files found.");
        }

        final int randomIndex = ThreadLocalRandom.current().nextInt(files.size());
        return files.get(randomIndex);
    }

    private static void validateDirectory(final Path directory) {
        Objects.requireNonNull(directory, "directory must not be null");

        if (!Files.exists(directory)) {
            throw new IllegalArgumentException("Directory does not exist: " + directory);
        }

        if (!Files.isDirectory(directory)) {
            throw new IllegalArgumentException("Path is not a directory: " + directory);
        }
    }
}
