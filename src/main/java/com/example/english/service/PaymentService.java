package com.example.english.service;

import com.example.english.dto.request.PaymentRequestDTO;
/*import com.mservice.models.PaymentResponse;*/
import javax.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;

public interface PaymentService {
  //ResponseEntity<PaymentResponse> createPaymentMomo(PaymentRequestDTO paymentRequestDTO, String returnUrl) throws Exception;
  ResponseEntity<?> createPaymentVNPay(PaymentRequestDTO paymentRequestDTO, String returnUrl);
  ResponseEntity<?> saveBill(String extraData, String amount, Integer resultCode);
  ResponseEntity<?> saveBillPaymentVNPay(HttpServletRequest request);
}
