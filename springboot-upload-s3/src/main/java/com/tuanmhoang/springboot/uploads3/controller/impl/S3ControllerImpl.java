package com.tuanmhoang.springboot.uploads3.controller.impl;

import com.tuanmhoang.springboot.uploads3.controller.S3Controller;
import com.tuanmhoang.springboot.uploads3.dto.DownloadFileResponse;
import com.tuanmhoang.springboot.uploads3.dto.GetDownloadPresignedUrlResponse;
import com.tuanmhoang.springboot.uploads3.dto.GetFilesResponse;
import com.tuanmhoang.springboot.uploads3.dto.GetUploadPresignedUrlResponse;
import com.tuanmhoang.springboot.uploads3.dto.UploadResponse;
import com.tuanmhoang.springboot.uploads3.exception.FileStorageServiceException;
import com.tuanmhoang.springboot.uploads3.services.FileStorageService;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
public class S3ControllerImpl implements S3Controller {

    private final FileStorageService fileStorageService;

    @Autowired
    public S3ControllerImpl(@Qualifier("fileStorageS3") FileStorageService fileStorageService) {
        this.fileStorageService = fileStorageService;
    }

    @Override
    public ResponseEntity<UploadResponse> upload(String accountId, MultipartFile file) {
        log.info("UUID: {}", accountId);
        try {
            fileStorageService.upload(accountId, file);
        } catch (IOException e) {
            throw new FileStorageServiceException("Error while uploading file", e);
        }
        return ResponseEntity.ok(new UploadResponse("OK"));
    }

    @Override
    public ResponseEntity<GetFilesResponse> getFiles(String accountId) {
        final List<String> files = fileStorageService.listFiles(accountId);
        return ResponseEntity.ok(GetFilesResponse.builder()
            .fileNames(files)
            .message("OK")
            .build());
    }

    @Override
    public ResponseEntity<DownloadFileResponse> downloadFile(String accountId, String fileName) {
        byte[] downloadedFile = new byte[0];
        try {
            downloadedFile = fileStorageService.downloadFile(accountId, fileName);
        } catch (IOException e) {
            throw new FileStorageServiceException("Error while downloading file", e);
        }
        return ResponseEntity.ok(DownloadFileResponse.builder()
            .fileData(downloadedFile)
            .message("OK")
            .build());
    }

    @Override
    public ResponseEntity<GetUploadPresignedUrlResponse> getUploadPresignedUrl(String accountId, String fileName) {
        String presignedUrl = fileStorageService.getUploadPresignedUrl(accountId, fileName);
        return ResponseEntity.ok(GetUploadPresignedUrlResponse.builder()
            .message("OK")
            .presignedUrl(presignedUrl)
            .build()
        );
    }

    @Override
    public ResponseEntity<GetDownloadPresignedUrlResponse> getDownloadPresignedUrl(String accountId, String fileName) {
        String presignedUrl = fileStorageService.getDownloadPresignedUrl(accountId, fileName);
        return ResponseEntity.ok(GetDownloadPresignedUrlResponse.builder()
            .message("OK")
            .presignedUrl(presignedUrl)
            .build()
        );
    }
}
