package mx.simio.transactionsdemo.api.service;

import mx.simio.transactionsdemo.api.model.Report;
import mx.simio.transactionsdemo.api.model.Sum;
import mx.simio.transactionsdemo.api.dto.TransactionDTO;

import java.util.List;

public interface TransactionService {
    TransactionDTO createTransaction(Long userId, TransactionDTO transactionDTO);
    List<TransactionDTO> readUserTransactions(Long userId);
    TransactionDTO readUserTransaction(Long userId, String transactionId);
    List<Report> readUserTransactionsReport(Long userId);
    Sum sumUserTransactions(Long userId);
    TransactionDTO readRandomTransaction();
}
