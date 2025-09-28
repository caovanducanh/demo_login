package com.example.demologin.serviceImpl;

import com.example.demologin.service.ObjectStorageService;
import io.minio.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class ObjectStorageServiceImpl implements ObjectStorageService {

    private final MinioClient minioClient;
    private final String bucketName;

    public ObjectStorageServiceImpl(MinioClient minioClient,
                                @Value("${minio.bucket}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
        initBucket();
    }
    @Override
    public void initBucket() {
        try {
            log.info("Initializing bucket " + bucketName);
            boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
            if (!exists) {
                minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize bucket", e);
        }
    }

    @Override
    public void uploadDocument(String objectName, String content) {
        try (InputStream stream = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .stream(stream, stream.available(), -1)
                            .contentType("text/plain")
                            .build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload document", e);
        }
    }

    @Override
    public String fetchDocument(String objectName) {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder().bucket(bucketName).object(objectName).build())) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("Failed to fetch document", e);
        }
    }

    @Override
    public void deleteDocument(String objectName) {
        try {
            minioClient.removeObject(
                    RemoveObjectArgs.builder().bucket(bucketName).object(objectName).build()
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to delete document", e);
        }
    }
}
