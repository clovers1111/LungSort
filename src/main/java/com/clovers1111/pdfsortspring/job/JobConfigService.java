package com.clovers1111.pdfsortspring.job;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

public interface JobConfigService {
    JobConfig createJobConfig(MultipartFile multipartFile) throws IOException;

    JobConfig getJobConfig(UUID jobId);
}
