package com.tuanmhoang.springboot.uploads3.services.impl;

import com.tuanmhoang.springboot.uploads3.services.FileStorageService;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Qualifier("fileStorageLocal")
public class FileStorageLocal implements FileStorageService {

    @Value("${upload.local.dir}")
    private String filePath;

    @Override
    public void upload(String accountId, MultipartFile file) {
        log.info("FileStorageLocal is chosen");
        File outputFile = new File(filePath);
        try {
            FileUtils.copyInputStreamToFile(file.getInputStream(), outputFile);
        } catch (IOException e) {
            log.error("Exception occurs: {}", e);
        }
    }

    @Override
    public List<String> listFiles(String accountId) {
        log.info("Getting files using local. Skipping this, focus S3.");
        return Collections.emptyList();
    }

    @Override
    public byte[] downloadFile(String accountId, String fileName) {
        log.info("Dowloading files using local. Skipping this, focus S3.");
        return null;
    }
}
