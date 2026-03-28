package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.job.JobConfig;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;

@Service
public class DirectoryWorkerServiceImpl implements DirectoryWorkerService {

    // Private helper methods
    public Path createDirectory(Path path) throws IOException {
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
