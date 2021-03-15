package mx.simio.transactionsdemo.api.service.impl;

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
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;

import static mx.simio.transactionsdemo.api.util.AppConstant.*;

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
            throw new ResourceNotFoundException(TRANSACTION_NOT_FOUND);
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
            throw new ResourceNotFoundException(TRANSACTION_NOT_FOUND);
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
        List<Transaction> transactionsFromUser = transactionRepository.findByUserId(userId);

        if (transactionsFromUser.isEmpty()) {
            throw new ResourceNotFoundException(TRANSACTIONS_NOT_FOUND);
        }

        AtomicReference<LocalDate> weekStartDate =
                new AtomicReference<>(transactionsFromUser.stream()
                        .findFirst()
                        .get().getDate()
                        .with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));
        AtomicReference<LocalDate> weekFinishDate =
                new AtomicReference<>(weekStartDate.get().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)));

        AtomicInteger count = new AtomicInteger(0);
        List<Report> reports = new ArrayList<>();

        if (weekFinishDate.get().getMonth() != weekStartDate.get().getMonth()) {
            weekFinishDate.set(weekStartDate.get().with(TemporalAdjusters.lastDayOfMonth()));
        }

        transactionsFromUser.forEach(transaction -> {
            if ((transaction.getDate().isAfter(weekStartDate.get()) || transaction.getDate().isEqual(weekStartDate.get()))
                    && (transaction.getDate().isBefore(weekFinishDate.get()) || transaction.getDate().isEqual(weekFinishDate.get()))) {
                count.getAndIncrement();
            } else {
                if (count.get() > 0) {
                    Report report = Report.builder()
                            .userId(transaction.getUser().getId())
                            .weekStartDate(weekStartDate.toString() + " " + weekStartDate.get().getDayOfWeek().toString())
                            .weekFinishDate(weekFinishDate.toString() + " " + weekFinishDate.get().getDayOfWeek().toString())
                            .quantity(count.get())
                            .build();
                    reports.add(report);

                    count.set(1);

                    if (transaction.getDate().getDayOfWeek().equals(DayOfWeek.MONDAY)) {
                        weekStartDate.set(transaction.getDate());
                    } else {
                        weekStartDate.set(transaction.getDate().with(TemporalAdjusters.previous(DayOfWeek.MONDAY)));
                    }

                    weekFinishDate.set(weekStartDate.get().with(TemporalAdjusters.next(DayOfWeek.SUNDAY)));

                    if (weekFinishDate.get().getMonth() != weekStartDate.get().getMonth()) {
                        weekFinishDate.set(weekStartDate.get().with(TemporalAdjusters.lastDayOfMonth()));
                    }
                }
            }
        });

        return reports;
    }

    @Override
    @Transactional(readOnly = true)
    public Sum sumUserTransactions(Long userId) {
        User userFromDb = userService.readUserById(userId);

        List<Transaction> transactions = transactionRepository.findByUserId(userFromDb.getId());

        if (transactions.isEmpty()) {
            throw new ResourceNotFoundException(TRANSACTIONS_NOT_FOUND);
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
            throw new ResourceNotFoundException(TRANSACTION_NOT_FOUND);
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
