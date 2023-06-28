package com.example.english.service;

import com.example.english.dto.request.PaymentRequestDTO;
import com.mservice.models.PaymentResponse;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
  ResponseEntity<PaymentResponse> createPaymentMomo(PaymentRequestDTO paymentRequestDTO, String returnUrl) throws Exception;
  ResponseEntity<?> saveBill(String billId, Integer resultCode);
}
