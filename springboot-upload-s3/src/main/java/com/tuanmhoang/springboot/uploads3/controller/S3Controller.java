package com.tuanmhoang.springboot.uploads3.controller;

import com.tuanmhoang.springboot.uploads3.dto.DownloadFileResponse;
import com.tuanmhoang.springboot.uploads3.dto.GetFilesResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.tuanmhoang.springboot.uploads3.dto.UploadResponse;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@RequestMapping("/s3")
public interface S3Controller {

    @PostMapping("/upload/{accountId}")
    ResponseEntity<UploadResponse> upload(@PathVariable String accountId, @RequestParam("file") MultipartFile file);

    @GetMapping("/listFiles/{accountId}")
    ResponseEntity<GetFilesResponse> getFiles(@PathVariable String accountId);

    @GetMapping("/downloadFile/{accountId}/{fileName}")
    ResponseEntity<DownloadFileResponse> downloadFile(@PathVariable String accountId, @PathVariable String fileName);
}
