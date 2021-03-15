package mx.simio.transactionsdemo.api.service.impl;

import mx.simio.transactionsdemo.api.dto.TransactionDTO;
import mx.simio.transactionsdemo.api.exception.ResourceNotFoundException;
import mx.simio.transactionsdemo.api.model.Report;
import mx.simio.transactionsdemo.api.model.Sum;
import mx.simio.transactionsdemo.api.model.entity.Transaction;
import mx.simio.transactionsdemo.api.model.entity.User;
import mx.simio.transactionsdemo.api.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static mx.simio.transactionsdemo.api.util.AppConstant.*;

class TransactionServiceImplTest {
    private static final Long USER_ID = 1L;
    private static final String TRANSACTION_UUID = "69370f68-2460-4e9d-a02e-9158c215576a";
    private static final String TRANSACTION_DESCRIPTION = "Tacos El Marrano";
    private static final String USER_NAME = "Adrián";
    private static final BigDecimal TRANSACTION_AMOUNT = new BigDecimal(10);
    private static final TransactionDTO TRANSACTION_DTO = new TransactionDTO();
    private static final LocalDate TRANSACTION_DATE = LocalDate.now();
    private static final Transaction TRANSACTION = new Transaction();
    private static final User USER = new User();
    private static final Report REPORT = new Report();

    @Mock
    UserServiceImpl userService;

    @Mock
    TransactionRepository transactionRepository;

    @InjectMocks
    TransactionServiceImpl transactionService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        TRANSACTION_DTO.setId(UUID.fromString(TRANSACTION_UUID));
        TRANSACTION_DTO.setAmount(TRANSACTION_AMOUNT);
        TRANSACTION_DTO.setDate(LocalDate.now().toString());
        TRANSACTION_DTO.setDescription(TRANSACTION_DESCRIPTION);
        TRANSACTION_DTO.setUserId(USER_ID);

        TRANSACTION.setId(UUID.fromString(TRANSACTION_UUID));
        TRANSACTION.setUser(USER);
        TRANSACTION.setAmount(TRANSACTION_AMOUNT);
        TRANSACTION.setDescription(TRANSACTION_DESCRIPTION);
        TRANSACTION.setDate(TRANSACTION_DATE);

        USER.setId(USER_ID);
        USER.setName(USER_NAME);

        REPORT.setUserId(USER_ID);
        REPORT.setQuantity(2);
        REPORT.setWeekFinishDate("2020-03-25 MONDAY");
        REPORT.setWeekFinishDate("2020-03-25 THURSDAY");
    }

    @Test
    void createTransactionTest() {
        Mockito.when(userService.readUserById(USER_ID)).thenReturn(USER);
        Mockito.when(transactionRepository.save(Mockito.any(Transaction.class))).thenReturn(TRANSACTION);

        final TransactionDTO transaction = transactionService.createTransaction(USER_ID, TRANSACTION_DTO);

        assertNotNull(transaction);
    }

    @Test
    void readUserTransactionsTest() {
        Mockito.when(userService.readUserById(USER_ID)).thenReturn(USER);
        Mockito.when(transactionRepository.findByUserId(USER_ID)).thenReturn(Collections.singletonList(TRANSACTION));

        final List<TransactionDTO> response = transactionService.readUserTransactions(USER_ID);

        assertNotNull(response);
        assertFalse(response.isEmpty());
        assertEquals(1, response.size());
    }

    @Test
    void readUserTransactionsTransactionNotFoundTest() {
        Mockito.when(userService.readUserById(USER_ID)).thenReturn(USER);
        Mockito.when(transactionRepository.findByUserId(USER_ID)).thenReturn(Collections.emptyList())
                .thenThrow(new ResourceNotFoundException(TRANSACTIONS_NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> transactionService.readUserTransactions(USER_ID));
    }

    @Test
    void readUserTransactionTest() {
        Mockito.when(transactionRepository.findByUserIdAndId(USER_ID, UUID.fromString(TRANSACTION_UUID)))
                .thenReturn(Optional.of(TRANSACTION));

        final TransactionDTO transaction = transactionService.readUserTransaction(USER_ID, TRANSACTION_UUID);

        assertNotNull(transaction);
    }

    @Test
    void readUserTransactionNotFoundTest() {
        Mockito.when(transactionRepository.findByUserIdAndId(USER_ID, UUID.fromString(TRANSACTION_UUID)))
                .thenReturn(Optional.empty()).thenThrow(new ResourceNotFoundException(TRANSACTION_NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> transactionService.readUserTransaction(USER_ID, TRANSACTION_UUID));
    }

    @Test
    void sumUserTransactionsTest() {
        Mockito.when(userService.readUserById(USER_ID)).thenReturn(USER);
        Mockito.when(transactionRepository.findByUserId(USER_ID)).thenReturn(Collections.singletonList(TRANSACTION));

        final Sum userTransactionsSum = transactionService.sumUserTransactions(USER_ID);

        assertNotNull(userTransactionsSum);
    }

    @Test
    void sumUserTransactionsTestWithNoTransactions() {
        Mockito.when(userService.readUserById(USER_ID)).thenReturn(USER);
        Mockito.when(transactionRepository.findByUserId(USER_ID)).thenReturn(Collections.emptyList())
                .thenThrow(new ResourceNotFoundException(TRANSACTIONS_NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> transactionService.sumUserTransactions(USER_ID));
    }

    @Test
    void readRandomTransactionTest() {
        Mockito.when(transactionRepository.findAll()).thenReturn(Collections.singletonList(TRANSACTION));

        final TransactionDTO transaction = transactionService.readRandomTransaction();

        assertNotNull(transaction);
        assertEquals(TRANSACTION_DTO, transaction);
    }

    @Test
    void readRandomTransactionNotFoundTest() {
        Mockito.when(transactionRepository.findAll()).thenReturn(Collections.emptyList())
                .thenThrow(new ResourceNotFoundException(TRANSACTION_NOT_FOUND));

        assertThrows(ResourceNotFoundException.class, () -> transactionService.readRandomTransaction());
    }
}
