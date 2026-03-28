package com.clovers1111.pdfsortspring;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Predicate;

public final class Config {
    private static Properties properties;
    private static final Logger logger = LoggerFactory.getLogger(Config.class);

    private static final Path directory;


    static {
        properties = new Properties();
        try (InputStream input = Config.class.getClassLoader().getResourceAsStream("application.properties")) {
            logger.debug("Initializing properties using {}", properties.toString());
            if (input == null) {
                throw new RuntimeException("Config file not found in classpath");
            }
            properties.load(input);
            directory = createDirectory();
            logger.info("Directory path initialized to {}", getDirectory());
        } catch (IOException ex) {
            throw new RuntimeException("Failed to load config file", ex);
        }

    }


    public static String getProperty(String key) {
        return properties.getProperty(key);
    }

    private static Path createDirectory() throws IOException {
        final String configuredPath = getProperty("directory");
        final File directory = (configuredPath != null && !configuredPath.isBlank())
                ? new File(configuredPath)
                : Files.createTempDirectory("temp").toFile();

        // Make this into a function to check for exceptions
        if (directory == null) {
            logger.error("Directory reference is null after initialization.");
            throw new IllegalStateException("Directory could not be initialized.");
        }

        if (!directory.exists()) {
            logger.debug("Directory does not exist, creating: {}", directory.getAbsolutePath());
            Files.createDirectories(directory.toPath());
        }

        if (!directory.isDirectory()) {
            logger.error("Path exists but is not a directory: {}", directory.getAbsolutePath());
            throw new NotDirectoryException(directory.getAbsolutePath());
        }

        if (!directory.canRead()) {
            logger.error("Directory is not readable: {}", directory.getAbsolutePath());
            throw new AccessDeniedException(directory.getAbsolutePath(), null, "Directory is not readable");
        }

        if (!directory.canWrite()) {
            logger.error("Directory is not writable: {}", directory.getAbsolutePath());
            throw new AccessDeniedException(directory.getAbsolutePath(), null, "Directory is not writable");
        }

        logger.debug("Directory validated successfully: {}", directory.getAbsolutePath());
        return directory.toPath();
    }

    public static Path getDirectory() {
        return directory;
    }

    public static Integer getIntProperty(String key) {
        String integerAsString = properties.getProperty(key);
        try {
            return Integer.parseInt(integerAsString);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static List<Integer> getIntListProperty(String key, String delimiter) {
        String value = properties.getProperty(key);
        if (value.isEmpty()) {
            // TODO: Fallback list needs to be implemented while staying decoupled
            logger.error("No DPIs found in properties.");
            return List.of();
        }
        return Arrays.stream(value.split(delimiter))
                .filter(Predicate.not(String::isBlank))
                .map(Integer::parseInt)
                .toList();
    }
}