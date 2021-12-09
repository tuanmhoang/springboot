package com.tuanmhoang.springboot.uploads3.services.impl;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.amazonaws.services.s3.model.S3ObjectInputStream;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.tuanmhoang.springboot.uploads3.exception.FileStorageServiceException;
import com.tuanmhoang.springboot.uploads3.exception.InvalidFileTypeException;
import com.tuanmhoang.springboot.uploads3.services.FileStorageService;
import java.io.IOException;
import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.tika.Tika;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Qualifier("fileStorageS3")
public class FileStorageS3 implements FileStorageService {

    public static final int SECONDS_IN_A_MINUTES = 60;

    public static final int MINUTES_IN_AN_HOUR = 60;

    public static final int MILLISECONDS_IN_A_SECOND = 1000;

    private final AmazonS3 s3Client = AmazonS3ClientBuilder.defaultClient();

    private final Tika tika = new Tika();

    private static final String SLASH = "/";

    private static final String[] LEGAL_FILE_EXTENSIONS = {
        MediaType.APPLICATION_PDF_VALUE,
        MediaType.IMAGE_JPEG_VALUE,
        MediaType.IMAGE_PNG_VALUE,
        MediaType.TEXT_PLAIN_VALUE,
        "application/zip",
    };

    @Value("${amazon.s3.bucketname}")
    private String bucketName;

    @Value("${amazon.s3.url.expiration.minutes}")
    private int expirationAsMinutes;

    @Override
    public void upload(String accountId, MultipartFile file) throws FileStorageServiceException {
        log.info("FileStorageS3 is chosen to upload. File to upload: {}", file.getOriginalFilename());

        String mimeType = getUploadFileMimeType(file);
        if (!isFileTypeValid(mimeType)) {
            throw new InvalidFileTypeException("File type is invalid: " + mimeType);
        }

        PutObjectRequest request = null;
        try {
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentType(mimeType);
            //metadata.addUserMetadata("title", "someTitle");
            request = new PutObjectRequest(bucketName,
                accountId + SLASH + file.getOriginalFilename(),
                file.getInputStream(),
                metadata
            );
            s3Client.putObject(request);
            log.info("Done upload file: {}", file.getOriginalFilename());
        } catch (IOException e) {
            throw new FileStorageServiceException("Error when putting file to S3", e);
        }
    }

    @Override
    public List<String> listFiles(String accountId) {
        ObjectListing listing = s3Client.listObjects(bucketName, accountId + "/");
        final List<S3ObjectSummary> objectSummaries = listing.getObjectSummaries();
        return objectSummaries
            .stream()
            .map(os -> os.getKey())
            .collect(Collectors.toList());
    }

    @Override
    public byte[] downloadFile(String accountId, String fileName) throws IOException {
        log.info("FileStorageS3 is chosen to download. File to download: {}", fileName);
        S3Object fullObject = s3Client.getObject(new GetObjectRequest(bucketName, accountId + SLASH + fileName));
        log.info("Content-Type: " + fullObject.getObjectMetadata().getContentType());
        final S3ObjectInputStream s3ObjectInputStream = fullObject.getObjectContent();
        try (s3ObjectInputStream) {
            return s3ObjectInputStream.readAllBytes();
        } catch (IOException e) {
            throw new FileStorageServiceException("Exception while download", e);
        }
    }

    @Override
    public String getUploadPresignedUrl(String accountId, String fileName) {
        // Generate the pre-signed URL.
        log.info("Generating pre-signed URL.");
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratedPresignedUrlRequest(
            accountId,
            fileName,
            getExpirationDate(),
            HttpMethod.PUT
        );
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    @Override
    public String getDownloadPresignedUrl(String accountId, String fileName) {
        // Generate the pre-signed URL.
        log.info("Generating pre-signed URL.");
        GeneratePresignedUrlRequest generatePresignedUrlRequest = getGeneratedPresignedUrlRequest(
            accountId,
            fileName,
            getExpirationDate(),
            HttpMethod.GET
        );
        URL url = s3Client.generatePresignedUrl(generatePresignedUrlRequest);
        return url.toString();
    }

    private Date getExpirationDate() {
        Date expiration = new Date();
        long expTimeMillis = expiration.getTime();
        expTimeMillis += (MILLISECONDS_IN_A_SECOND * SECONDS_IN_A_MINUTES * MINUTES_IN_AN_HOUR) * expirationAsMinutes / MINUTES_IN_AN_HOUR;
        expiration.setTime(expTimeMillis);
        return expiration;
    }

    private GeneratePresignedUrlRequest getGeneratedPresignedUrlRequest(String accountId, String fileName, Date expiration,
        HttpMethod httpMethod) {
        return new GeneratePresignedUrlRequest(bucketName,
            accountId + SLASH + fileName)
            .withExpiration(expiration)
            .withMethod(httpMethod);
    }

    private boolean isFileTypeValid(String mimeType) {
        return !ObjectUtils.isEmpty(mimeType) &&
            Arrays.asList(LEGAL_FILE_EXTENSIONS).contains(mimeType);
    }

    private String getUploadFileMimeType(MultipartFile file) throws FileStorageServiceException {
        String mimeType = null;
        try {
            mimeType = tika.detect(file.getInputStream());
            log.info("mimeType: {}", mimeType);
        } catch (IOException e) {
            throw new FileStorageServiceException("Error when detecting file mime type,", e);
        }
        return mimeType;
    }
}
