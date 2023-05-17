package com.example.english.utils;

import com.example.english.service.StorageService;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

public class Utils {
  @Autowired private static StorageService storageService;
  public static final String  DEFAULT_PAGE_SIZE = "10";
  public static final String DEFAULT_PAGE_NUMBER = "1";

  public static List<String> storeFile(MultipartFile[] images) throws IOException {
    int numberOfDocument = images.length;
    String[] nameFiles = new String[numberOfDocument];
    for (int i=0; i<numberOfDocument; i++){
      nameFiles[i] = storageService.uploadFile(images[i]);
    }

    return new ArrayList<>(Arrays.asList(nameFiles));
  }
}
