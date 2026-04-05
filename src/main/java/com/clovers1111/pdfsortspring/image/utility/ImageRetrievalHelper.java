package com.clovers1111.pdfsortspring.image.utility;

import com.clovers1111.pdfsortspring.file.FileTypes;
import com.clovers1111.pdfsortspring.file.utility.FileRetrievalHelper;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Stream;

public class ImageRetrievalHelper extends FileRetrievalHelper {

    protected ImageRetrievalHelper() {
    }

    public static Path getRandomImageFile(final Path directory) throws IOException {
        return pickRandomFile(listRegularFiles(directory).stream()
                .filter(ImageRetrievalHelper::isImageFile)
                .toList());
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

    private static boolean isImageFile(final Path path) {
        return FileTypes.fromExtension(getFileExtension(path))
                .map(FileTypes::isImage)
                .orElse(false);
    }

}

