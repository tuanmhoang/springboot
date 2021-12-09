package com.tuanmhoang.springboot.uploads3.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GetDownloadPresignedUrlResponse {
    private String message;
    private String presignedUrl;
}
