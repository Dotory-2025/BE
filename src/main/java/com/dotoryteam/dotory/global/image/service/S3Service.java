package com.dotoryteam.dotory.global.image.service;

import com.dotoryteam.dotory.global.common.utils.GlobalFileValidator;
import com.dotoryteam.dotory.global.image.dto.response.PresignedUrlResponse;
import com.dotoryteam.dotory.global.image.enums.FileDirectory;
import com.dotoryteam.dotory.global.image.exception.FileSizeTooBigException;
import com.dotoryteam.dotory.global.image.utils.S3FilePathResolver;
import com.dotoryteam.dotory.global.image.utils.S3Uploader;
import com.dotoryteam.dotory.global.image.utils.S3UrlGenerator;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;


@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3FilePathResolver pathResolver;
    private final GlobalFileValidator fileValidator;
    private final S3Uploader s3Uploader;
    private final S3UrlGenerator urlGenerator;
    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.cloudfront.domain}")
    private String cloudFrontDomain;

    public PresignedUrlResponse getPresignedUrl(String prefix, String originalFilename , long fileSize) {
        FileDirectory directory = FileDirectory.of(prefix);

        if (fileSize > directory.getFileSize()) {
            throw new FileSizeTooBigException(directory.getFileSize());
        }

        String contentType = fileValidator.validateAndGetExtension(originalFilename).getMimeType();
        String key = pathResolver.createPath(directory.getPrefix() , originalFilename);
        PutObjectRequest objectRequest = s3Uploader.createPutObjectRequest(key , contentType , fileSize);

        String url = urlGenerator.createPresignedUrl(directory.getUploadTime() , objectRequest);

        return new PresignedUrlResponse(url , key);
    }

    private void deleteFile(String fileKey) {
        if (!StringUtils.hasText(fileKey)) return;

        s3Template.deleteObject(bucket , fileKey);
    }

    public String getCloudFrontUrl(String storedFileName) {
        return cloudFrontDomain + "/" + storedFileName;
    }
}