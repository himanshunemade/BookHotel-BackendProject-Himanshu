package com.sweethome.paymentservice.service;

import com.sweethome.paymentservice.entity.TransactionDetailsEntity;
import com.sweethome.paymentservice.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class TransactionService {
    @Autowired
    TransactionRepository transactionRepository;

    @Transactional
    public Integer createTransaction(TransactionDetailsEntity transactionDetails) {
        TransactionDetailsEntity entity = transactionRepository.save(transactionDetails);
        return entity.getTransactionId();
    }

    public TransactionDetailsEntity getTransaction(int id) {
        Optional<TransactionDetailsEntity> optional = transactionRepository.findById(id);
        return optional.get();
    }
}
