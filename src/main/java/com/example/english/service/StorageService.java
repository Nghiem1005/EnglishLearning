package com.example.english.service;

import com.google.cloud.storage.Blob;
import java.io.IOException;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

public interface StorageService {
  String uploadFile(MultipartFile file) throws IOException;
  void deleteFile(String fileName);
  Blob getFile(String fileName);
  Mono<Resource> readVideoContent(String fileName);
}
