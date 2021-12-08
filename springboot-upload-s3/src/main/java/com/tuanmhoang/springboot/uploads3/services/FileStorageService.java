package com.tuanmhoang.springboot.uploads3.services;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    void upload(String accountId, MultipartFile file) throws IOException;

    List<String> listFiles(String accountId);

    byte[] downloadFile(String accountId, String fileName) throws IOException;
}
