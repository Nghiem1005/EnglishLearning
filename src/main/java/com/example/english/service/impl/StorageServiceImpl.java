package com.example.english.service.impl;

import com.example.english.exceptions.ResourceNotFoundException;
import com.example.english.service.StorageService;
import com.example.english.utils.Converter;
import com.google.cloud.storage.Blob;
import com.google.cloud.storage.Bucket;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import com.google.firebase.cloud.StorageClient;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

@Service
public class StorageServiceImpl implements StorageService {

  private final Storage storage;

  public StorageServiceImpl() {
    this.storage = StorageOptions.newBuilder().setProjectId("englishlearningweb").build().getService();
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
  public String uploadFile(MultipartFile file) throws IOException {
    Bucket bucket = StorageClient.getInstance().bucket();

    String name = generateFileName(file);

    Blob blob = bucket.create(name, file.getBytes(), file.getContentType());

    return "https://storage.googleapis.com/englishlearningweb.appspot.com/" + blob.getName();
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

  @Override
  public Mono<Resource> readVideoContent(String fileName) {
    Bucket bucket = StorageClient.getInstance().bucket();

    Blob blob = bucket.get(fileName);
    Resource resource = Converter.blobToResource(blob);
    return Mono.fromSupplier(()->resource);

  }

}