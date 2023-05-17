package com.example.english.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import java.io.IOException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

@Configuration
public class FirebaseConfig {
  @Bean
  public FirebaseApp firebaseApp() throws IOException {
    ClassPathResource serviceAccount = new ClassPathResource("serviceAccountKey.json");
    FirebaseOptions options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
        .setStorageBucket("englishlearningweb.appspot.com")
        .build();
    return FirebaseApp.initializeApp(options);
  }

}
