package com.example.english.controller;

import com.example.english.dto.request.PaymentRequestDTO;
import com.example.english.dto.response.ResponseObject;
import com.example.english.service.PaymentService;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.view.RedirectView;

@RestController
public class PaymentController {
  public static final String SUCCESS_URL = "success";
  public static final String CANCEL_URL = "cancel";

  public static final String RETURN_URL = "courses";
  @Autowired private PaymentService paymentService;

  /*@PostMapping(value = "/momo")
  public ResponseEntity<PaymentResponse> momo(@RequestBody PaymentRequestDTO paymentRequestDTO) throws Exception{
    return paymentService.createPaymentMomo(paymentRequestDTO,
        "http://localhost:8080/" + SUCCESS_URL);
  }*/

  @GetMapping(value = "/success")
  public RedirectView successPay(@RequestParam(name = "amount") String amount, @RequestParam(name = "extraData") String extraData, @RequestParam(name = "resultCode") Integer resultCode) {
    paymentService.saveBill(extraData, amount, resultCode);
    return new RedirectView("http://localhost:3002/" + RETURN_URL);
  }

  @PostMapping("/vn_pay")
  public ResponseEntity<?> vnPay(@RequestBody PaymentRequestDTO paymentRequestDTO){
    String returnUrl = "http://localhost:8080/vnpay-payment";
    return paymentService.createPaymentVNPay(paymentRequestDTO, returnUrl);
  }

  @GetMapping("/vnpay-payment")
  public RedirectView GetMapping(HttpServletRequest request)throws IOException, ServletException {
    paymentService.saveBillPaymentVNPay(request);
    return new RedirectView("http://localhost:3002/" + RETURN_URL);

  }
}
