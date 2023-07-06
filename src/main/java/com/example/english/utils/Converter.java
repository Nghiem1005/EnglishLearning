package com.example.english.utils;

import com.google.cloud.storage.Blob;
import java.util.Calendar;
import java.util.Date;
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

  public static Date timeDateToZero(Date date) {
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);

    // Đặt giờ, phút và giây về 0
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);

    Date convertedDate = calendar.getTime();
    return convertedDate;
  }
}
