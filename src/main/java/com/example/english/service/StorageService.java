package com.example.english.service;

import com.google.cloud.storage.Blob;
import java.io.IOException;
import org.springframework.web.multipart.MultipartFile;

public interface StorageService {
  String uploadFile(MultipartFile file) throws IOException;
  void deleteFile(String fileName);
  Blob getFile(String fileName);
}
