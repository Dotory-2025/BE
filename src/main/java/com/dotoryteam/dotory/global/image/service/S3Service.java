package com.dotoryteam.dotory.global.image.service;

import com.dotoryteam.dotory.global.common.enums.AcceptableFileType;
import com.dotoryteam.dotory.global.common.utils.GlobalFileValidator;
import com.dotoryteam.dotory.global.image.dto.response.PresignedUrlResponse;
import com.dotoryteam.dotory.global.image.utils.S3FilePathResolver;
import com.dotoryteam.dotory.global.image.utils.S3Uploader;
import com.dotoryteam.dotory.global.image.utils.S3UrlGenerator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PutObjectPresignRequest;

import java.time.Duration;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class S3Service {
    private final S3FilePathResolver pathResolver;
    private final GlobalFileValidator fileValidator;
    private final S3Uploader s3Uploader;
    private final S3UrlGenerator urlGenerator;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${spring.cloud.aws.cloudfront.domain}")
    private String cloudFrontDomain;

    //presigned 주소 받기
    public PresignedUrlResponse getPresignedUrl(String prefix, String originalFilename) {
        String contentType = fileValidator.validateAndGetExtension(originalFilename).getMimeType();

        String key = pathResolver.createPath(prefix , originalFilename);

        PutObjectRequest objectRequest = s3Uploader.createPutObjectRequest(key , contentType);

        String url = urlGenerator.createPresignedUrl(objectRequest);

        return new PresignedUrlResponse(url , key);
    }

    // 2. DB에 저장하거나 프론트엔드에 보여줄 때는 CloudFront URL 반환
    public String getCloudFrontUrl(String storedFileName) {
        return cloudFrontDomain + "/" + storedFileName;
    }
}