package com.clovers1111.pdfsortspring.image.utility;

import com.clovers1111.pdfsortspring.Config;
import com.clovers1111.pdfsortspring.job.JobConfig;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

public class ImagePathToUrlMapper {
    private final static String MVC_IMAGES_PATH = Config.getProperty("mvc-images-path");


    public static Set<String> imagePathToUrl(Set<Path> imageFilePaths, JobConfig jobConfig) {
        return imageFilePaths.stream()
                .map(path ->  MVC_IMAGES_PATH + jobConfig.getJobId() + "/" + path.getFileName())
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));

    }

}
