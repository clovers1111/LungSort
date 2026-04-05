package com.clovers1111.pdfsortspring.pdf.utility;

import com.clovers1111.pdfsortspring.file.FileTypes;
import com.clovers1111.pdfsortspring.file.utility.FileRetrievalHelper;

import java.io.IOException;
import java.nio.file.Path;

public class PdfRetrievalHelper extends FileRetrievalHelper {
    public static Path getRandomPdfFile(final Path directory) throws IOException {
        return pickRandomFile(listRegularFiles(directory).stream()
                .filter(path -> FileTypes.fromExtension(getFileExtension(path))
                        .filter(FileTypes.PDF::equals)
                        .isPresent())
                .toList());
    }
}
