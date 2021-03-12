package mx.simio.transactionsdemo.api.service.impl;

import lombok.extern.slf4j.Slf4j;
import mx.simio.transactionsdemo.api.dto.TransactionDTO;
import mx.simio.transactionsdemo.api.exception.ResourceNotFoundException;
import mx.simio.transactionsdemo.api.model.Report;
import mx.simio.transactionsdemo.api.model.Sum;
import mx.simio.transactionsdemo.api.model.entity.Transaction;
import mx.simio.transactionsdemo.api.model.entity.User;
import mx.simio.transactionsdemo.api.repository.TransactionRepository;
import mx.simio.transactionsdemo.api.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

@Slf4j
@Service
public class TransactionServiceImpl implements TransactionService {
    private final UserServiceImpl userService;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(UserServiceImpl userService, TransactionRepository transactionRepository) {
        this.userService = userService;
        this.transactionRepository = transactionRepository;
    }

    @Override
    @Transactional
    public TransactionDTO createTransaction(Long userId, TransactionDTO transactionDTO) {
        User existingUser = userService.readUserById(userId);

        Transaction transactionToSave = Transaction.builder()
                .amount(transactionDTO.getAmount())
                .description(transactionDTO.getDescription())
                .date(LocalDate.now())
                .user(existingUser)
                .build();

        Transaction savedTransaction = transactionRepository.save(transactionToSave);

        return TransactionDTO.builder()
                .id(savedTransaction.getId())
                .amount(savedTransaction.getAmount())
                .description(savedTransaction.getDescription())
                .date(savedTransaction.getDate().toString())
                .userId(savedTransaction.getUser().getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionDTO> readUserTransactions(Long userId) {
        User userFromDb = userService.readUserById(userId);

        List<Transaction> transactions = transactionRepository.findByUserId(userFromDb.getId());

        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("Transactions not found");
        }

        Transaction.sortTransactionsChronologically(transactions);

        List<TransactionDTO> transactionDTOS = new ArrayList<>();

        transactions.forEach(transaction -> {
            TransactionDTO transactionDTO = TransactionDTO.builder()
                    .id(transaction.getId())
                    .amount(transaction.getAmount())
                    .description(transaction.getDescription())
                    .date(transaction.getDate().toString())
                    .userId(transaction.getUser().getId())
                    .build();

            transactionDTOS.add(transactionDTO);
        });

        return transactionDTOS;
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDTO readUserTransaction(Long userId, String transactionId) {
        Optional<Transaction> transactionFromDb = transactionRepository.findByUserIdAndId(userId, UUID.fromString(transactionId));

        if (transactionFromDb.isEmpty()) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        return TransactionDTO.builder()
                .id(transactionFromDb.get().getId())
                .amount(transactionFromDb.get().getAmount())
                .description(transactionFromDb.get().getDescription())
                .date(transactionFromDb.get().getDate().toString())
                .userId(transactionFromDb.get().getUser().getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Report> readUserTransactionsReport(Long userId) {
        return null;
    }

    @Override
    @Transactional(readOnly = true)
    public Sum sumUserTransactions(Long userId) {
        User userFromDb = userService.readUserById(userId);

        List<Transaction> transactions = transactionRepository.findByUserId(userFromDb.getId());

        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException("Transactions not found");
        }

        BigDecimal sum = transactions.stream().map(Transaction::getAmount).reduce(BigDecimal.ZERO, BigDecimal::add);

        return Sum.builder()
                .sum(sum)
                .userId(userFromDb.getId())
                .build();
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionDTO readRandomTransaction() {
        List<Transaction> transactions = transactionRepository.findAll();

        Collections.shuffle(transactions);

        Optional<Transaction> transaction = transactions.stream().findFirst();

        if (transaction.isEmpty()) {
            throw new ResourceNotFoundException("Transaction not found");
        }

        return TransactionDTO.builder()
                .id(transaction.get().getId())
                .userId(transaction.get().getUser().getId())
                .amount(transaction.get().getAmount())
                .description(transaction.get().getDescription())
                .date(transaction.get().getDate().toString())
                .build();
    }
}
