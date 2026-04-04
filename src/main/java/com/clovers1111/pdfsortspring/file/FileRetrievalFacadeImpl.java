package com.clovers1111.pdfsortspring.file;

import com.clovers1111.pdfsortspring.file.utility.FileRetrievalService;
import com.clovers1111.pdfsortspring.job.JobConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class FileRetrievalFacadeImpl implements FileRetrievalFacade {

    final static Logger logger = LoggerFactory.getLogger(FileRetrievalFacadeImpl.class);

    // Keep track of the job config and corresponding paths of image files
    private final static Map<JobConfig, Stack<Path>> cache = new HashMap<>();

    @Override
    public Set<Path> retrieveImageFiles(final JobConfig jobConfig, final Integer numOfFiles) {
        if (jobConfig == null) {
            logger.warn("JobConfig {} is null and cannot retrieve image files", jobConfig.getJobId());
            return null;
        }

        final Stack<Path> pathStack = getCache(jobConfig);
        Set<Path> paths = new HashSet<>();

        if (pathStack.isEmpty()) {
            logger.warn("JobConfig {} has no files", jobConfig.getJobId());
            return Set.of();
        } else if (pathStack.size() < numOfFiles) {
            logger.warn("JobConfig {} has less than {} files", jobConfig.getJobId(), numOfFiles);
            for (int i = 0; i < pathStack.size(); i++) {
                paths.add(pathStack.pop());
            }
        } else {
            for (int i = 0; i < numOfFiles; i++) {
                paths.add(pathStack.pop());
            }
        }
        return paths;
    }

    private Stack<Path> getCache(JobConfig jobConfig) {
        if (!cache.containsKey(jobConfig)) {
            logger.info("Caching job {}", jobConfig.getJobId());
            final Stack<Path> pathStack = Stream.of(FileRetrievalService.listImageFiles(
                            jobConfig.getJobDir()))
                    .peek(Collections::shuffle)
                    .flatMap(List::stream)
                    .collect(Collectors.toCollection(Stack::new));

            cache.put(jobConfig, pathStack);
        }

        return cache.get(jobConfig);

    }




}
