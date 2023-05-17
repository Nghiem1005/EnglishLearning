package com.example.english.service.impl;

import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.service.StorageService;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.cloud.StorageClient;
import java.io.IOException;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class StorageServiceImpl implements StorageService {

  private final Storage storage;

  public StorageServiceImpl() {
    this.storage = StorageOptions.newBuilder().setProjectId("englishlearningweb").build().getService();
  }

  public String uploadFile(MultipartFile file) throws IOException {
    Bucket bucket = StorageClient.getInstance().bucket();

    String name = generateFileName(file);

    bucket.create(name, file.getBytes(), file.getContentType());

    return name;
  }

  private String generateFileName(MultipartFile file) {
    String name = "";

    if (file.getContentType().equals("application/pdf")) {
      name = file.getOriginalFilename();
    } else {
      name = file.getContentType() + "_" + UUID.randomUUID();
    }
    return name;
  }

  @Override
  public void deleteFile(String fileName) {
    Bucket bucket = StorageClient.getInstance().bucket();

    Blob blob = bucket.get(fileName);

    if (blob == null) {
      throw new ResourceNotFoundException("file not found");
    }

    blob.delete();
  }

  @Override
  public Blob getFile(String fileName) {
    Bucket bucket = StorageClient.getInstance().bucket();

    Blob blob = bucket.get(fileName);
    return blob;
  }

}