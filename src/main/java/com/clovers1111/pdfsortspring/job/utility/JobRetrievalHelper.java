package com.clovers1111.pdfsortspring.job.utility;

import com.clovers1111.pdfsortspring.file.FileTypes;
import com.clovers1111.pdfsortspring.file.utility.FileRetrievalHelper;

import java.nio.file.Path;
import java.util.UUID;

public class JobRetrievalHelper extends FileRetrievalHelper {

    // TODO: Centralize the naming procedure for job files to reducing coupling
    public static Path getJobConfig(final Path path, final UUID jobId) {
        final Path jobFile = Path.of(jobId.toString().replace("/", "").trim().concat(FileTypes.JSON.getExtension()));
        return path.resolve(jobFile); // Returns UUID/UUID.json
    }
}
