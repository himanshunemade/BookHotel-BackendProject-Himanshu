package com.sweethome.paymentservice.controller;

import com.sweethome.paymentservice.entity.TransactionDetailsEntity;
import com.sweethome.paymentservice.enums.PaymentMode;
import com.sweethome.paymentservice.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/payment")
public class TransactionController {

    @Autowired
    TransactionService service;

    @PostMapping("/transaction")
    public ResponseEntity<Integer> createTransaction(@RequestBody TransactionDetailsEntity transactionDetails) throws Exception {
        if(transactionDetails.getPaymentMode().equals(PaymentMode.UPI.toString()) || transactionDetails.getPaymentMode().equals(PaymentMode.CARD.toString())) {
            if (transactionDetails.getPaymentMode().equals(PaymentMode.CARD.toString()) && (transactionDetails.getCardNumber() == null || transactionDetails.getCardNumber().isBlank())) {
                throw new Exception("Card number must be provided");
            } else if (transactionDetails.getPaymentMode().equals(PaymentMode.UPI.toString()) && (transactionDetails.getUpiId() == null || transactionDetails.getUpiId().isBlank())) {
                throw new Exception("UPI Id must be provided");
            } else{
                 int transId = service.createTransaction(transactionDetails);
                 return new ResponseEntity<Integer>(transId, HttpStatus.CREATED);
            }
        } else{
            throw new Exception("Invalid mode of payment");
        }
    }

    @GetMapping("/transaction/{transactionId}")
    public ResponseEntity<TransactionDetailsEntity> getTransaction(@PathVariable String transactionId){
        int id = Integer.valueOf(transactionId);
        TransactionDetailsEntity entity = service.getTransaction(id);
        return new ResponseEntity<TransactionDetailsEntity>(entity,HttpStatus.OK);
    }

}
