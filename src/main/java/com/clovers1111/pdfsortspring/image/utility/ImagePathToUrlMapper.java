package com.clovers1111.pdfsortspring.image.utility;

import com.clovers1111.pdfsortspring.config.AppWebProperties;
import com.clovers1111.pdfsortspring.job.JobConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
public class ImagePathToUrlMapper {

    private final AppWebProperties appWebProperties;


    public Set<String> imagePathToUrl(Set<Path> imageFilePaths, JobConfig jobConfig) {
        return imageFilePaths.stream()
                .map(path -> appWebProperties.mvcImages() + jobConfig.getJobId() + "/" + path.getFileName())
                .collect(java.util.stream.Collectors.toCollection(LinkedHashSet::new));

    }

}
