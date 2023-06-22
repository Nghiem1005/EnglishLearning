package com.example.english.service;

import com.example.english.dto.request.MailRequestDTO;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;

public interface MailService {
  void sendFormRegister(String siteUrl, MailRequestDTO mailRequestDTO)
      throws MessagingException, UnsupportedEncodingException;

  void sendNewPassword(String siteUrl, MailRequestDTO mailRequestDTO)
      throws MessagingException, UnsupportedEncodingException;
}
