package com.clovers1111.pdfsortspring.controller;

import com.clovers1111.pdfsortspring.file.FileStorageService;
import com.clovers1111.pdfsortspring.job.JobConfig;
import com.clovers1111.pdfsortspring.job.JobConfigService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;
import org.mockito.InOrder;

class FileUploadControllerTest {

    private FileStorageService fileStorageService;
    private JobConfigService jobConfigService;
    private FileUploadController fileUploadController;

    @BeforeEach
    void setUp() {
        fileStorageService = mock(FileStorageService.class);
        jobConfigService = mock(JobConfigService.class);

        fileUploadController = new FileUploadController(fileStorageService,  jobConfigService);
    }

    @Test
    void uploadFile_returnsOkAndPersistsCreatedJob() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");
        when(multipartFile.getContentType()).thenReturn("application/pdf");
        when(multipartFile.getSize()).thenReturn(128L);

        JobConfig jobConfig = mock(JobConfig.class);
        when(jobConfig.getJobId()).thenReturn(UUID.randomUUID());
        when(jobConfig.getFileNameWithExtension()).thenReturn("test.pdf");
        when(jobConfig.getJobDir()).thenReturn(Path.of("/tmp/job-1"));
        when(jobConfigService.createJobConfig(multipartFile)).thenReturn(jobConfig);

        ResponseEntity<JobConfig> response = fileUploadController.uploadFile(multipartFile);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertSame(jobConfig, response.getBody());

        verify(jobConfigService).createJobConfig(multipartFile);
        verify(fileStorageService).saveMultipartFile(multipartFile, jobConfig);
        InOrder inOrder = inOrder(jobConfigService, fileStorageService);
        inOrder.verify(jobConfigService).createJobConfig(multipartFile);
        inOrder.verify(fileStorageService).saveMultipartFile(multipartFile, jobConfig);
    }

    @Test
    void uploadFile_rethrowsIOExceptionWhenStorageFails() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");
        when(multipartFile.getContentType()).thenReturn("application/pdf");
        when(multipartFile.getSize()).thenReturn(128L);

        JobConfig jobConfig = mock(JobConfig.class);
        when(jobConfig.getJobId()).thenReturn(UUID.randomUUID());
        when(jobConfig.getFileNameWithExtension()).thenReturn("test.pdf");
        when(jobConfig.getJobDir()).thenReturn(Path.of("/tmp/job-2"));
        when(jobConfigService.createJobConfig(multipartFile)).thenReturn(jobConfig);
        doThrow(new IOException("Disk write failed")).when(fileStorageService)
                .saveMultipartFile(multipartFile, jobConfig);

        assertThrows(IOException.class, () -> fileUploadController.uploadFile(multipartFile));

        verify(jobConfigService).createJobConfig(multipartFile);
        verify(fileStorageService).saveMultipartFile(multipartFile, jobConfig);
    }

    @Test
    void uploadFile_rethrowsIOExceptionWhenJobCreationFails() throws IOException {
        MultipartFile multipartFile = mock(MultipartFile.class);
        when(multipartFile.getOriginalFilename()).thenReturn("test.pdf");
        when(multipartFile.getContentType()).thenReturn("application/pdf");
        when(multipartFile.getSize()).thenReturn(128L);

        when(jobConfigService.createJobConfig(multipartFile)).thenThrow(new IOException("create failed"));

        assertThrows(IOException.class, () -> fileUploadController.uploadFile(multipartFile));

        verify(jobConfigService).createJobConfig(multipartFile);
        verifyNoInteractions(fileStorageService);
    }
}


