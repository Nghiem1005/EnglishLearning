package com.example.english.utils;

import com.google.cloud.storage.Blob;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;

public class Converter {
  public static Resource blobToResource(Blob blob) {
    byte[] bytes = blob.getContent();
    return new ByteArrayResource(bytes) {
      @Override
      public String getFilename() {
        return blob.getName();
      }
    };
  }
}
