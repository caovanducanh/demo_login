package com.example.demologin.serviceImpl;

import com.example.demologin.service.LessonPlanStorageService;
import io.minio.GetObjectArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

@Service
public class LessonPlanStorageServiceImpl implements LessonPlanStorageService {
    private final MinioClient minioClient;
    private final String bucketName;

    public LessonPlanStorageServiceImpl(MinioClient minioClient, @Value("${minio.bucket}") String bucketName) {
        this.minioClient = minioClient;
        this.bucketName = bucketName;
    }

    public void uploadContent(String objectKey, String content) throws Exception {
        try (ByteArrayInputStream bais = new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8))) {
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectKey)
                            .stream(bais, content.length(), -1)
                            .contentType("application/json")
                            .build()
            );
        }
    }

    public String downloadContent(String objectKey) throws Exception {
        try (InputStream stream = minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectKey)
                        .build()
        )) {
            return new String(stream.readAllBytes(), StandardCharsets.UTF_8);
        }
    }
}
