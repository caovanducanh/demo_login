package com.example.demologin.service;

public interface ObjectStorageService {
    void initBucket();
    void uploadDocument(String objectName, String content);
    String fetchDocument(String objectName);
    void deleteDocument(String objectName);
}
