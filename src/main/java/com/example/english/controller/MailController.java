package com.example.english.controller;


import com.example.english.dto.request.MailRequestDTO;
import com.example.english.service.MailService;
import java.io.UnsupportedEncodingException;
import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MailController {
  @Autowired private MailService mailService;
  @PostMapping(value = "/sendMail")
  public void sendFormRegister(@RequestBody MailRequestDTO mailRequestDTO, HttpServletRequest request)
      throws MessagingException, UnsupportedEncodingException {
    String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
    mailService.sendFormRegister(siteUrl, mailRequestDTO);
  }

  @PostMapping(value = "/newPassword")
  public void sendNewPassword(@RequestBody MailRequestDTO mailRequestDTO, HttpServletRequest request)
      throws MessagingException, UnsupportedEncodingException {
    String siteUrl = request.getRequestURL().toString().replace(request.getServletPath(), "");
    mailService.sendNewPassword(siteUrl, mailRequestDTO);
  }
}
