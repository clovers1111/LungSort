package com.clovers1111.pdfsortspring.file.utility;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;


public class DirectoryHelper {

    // Private helper methods
    public static Path createDirectory(Path path) throws IOException {
        try {
            return Files.createDirectory(path);
        } catch (FileAlreadyExistsException e) {
            System.err.println("Error: A file with the same name as the directory already exists: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("I/O error occurred: " + e.getMessage());
        }
        return null;
    }
}
