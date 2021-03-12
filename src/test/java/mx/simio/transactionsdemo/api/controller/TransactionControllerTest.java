package mx.simio.transactionsdemo.api.controller;

import mx.simio.transactionsdemo.api.dto.TransactionDTO;
import mx.simio.transactionsdemo.api.model.Report;
import mx.simio.transactionsdemo.api.model.Sum;
import mx.simio.transactionsdemo.api.service.impl.TransactionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TransactionControllerTest {
    private static final Long USER_ID = 1L;
    private static final String TRANSACTION_UUID = "69370f68-2460-4e9d-a02e-9158c215576a";
    private static final List<TransactionDTO> TRANSACTIONS_DTO = new ArrayList<>();
    private static final TransactionDTO TRANSACTION_DTO = new TransactionDTO();
    private static final Sum SUM = new Sum();
    private static final List<Report> LIST_REPORT = new ArrayList<>();

    @Mock
    TransactionServiceImpl transactionService;

    @InjectMocks
    TransactionController transactionController;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);

        TRANSACTION_DTO.setId(UUID.fromString(TRANSACTION_UUID));
        TRANSACTION_DTO.setAmount(new BigDecimal(15));
        TRANSACTION_DTO.setDate(LocalDate.now().toString());
        TRANSACTION_DTO.setDescription("Tacos El Marrano");
        TRANSACTION_DTO.setUserId(1L);

        SUM.setSum(new BigDecimal(100));
        SUM.setUserId(USER_ID);

        Mockito.when(transactionService.readUserTransactions(USER_ID)).thenReturn(TRANSACTIONS_DTO);
        Mockito.when(transactionService.createTransaction(USER_ID, TRANSACTION_DTO)).thenReturn(TRANSACTION_DTO);
        Mockito.when(transactionService.sumUserTransactions(USER_ID)).thenReturn(SUM);
        Mockito.when(transactionService.readUserTransaction(USER_ID, TRANSACTION_UUID)).thenReturn(TRANSACTION_DTO);
        Mockito.when(transactionService.readRandomTransaction()).thenReturn(TRANSACTION_DTO);
    }

    @Test
    public void createTransactionTest() {
        final ResponseEntity<TransactionDTO> response = transactionController.createTransaction(USER_ID, TRANSACTION_DTO);
        assertEquals(response.getStatusCode(), HttpStatus.CREATED);
        assertEquals(response.getStatusCodeValue(), HttpStatus.CREATED.value());
        assertEquals(response.getBody(), TRANSACTION_DTO);
    }
    
    @Test
    public void readUserTransactionsTest() {
        final ResponseEntity<List<TransactionDTO>> response = transactionController.readUserTransactions(USER_ID);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getBody(), TRANSACTIONS_DTO);
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
    }

    @Test
    public void sumUserTransactionsTest() {
        final ResponseEntity<Sum> response = transactionController.sumUserTransactions(USER_ID);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(response.getBody(), SUM);
    }

    @Test
    public void readUserTransactionTest() {
        final ResponseEntity<TransactionDTO> response = transactionController.readUserTransaction(USER_ID, TRANSACTION_UUID);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(response.getBody(), TRANSACTION_DTO);
    }

    @Test
     public void readUserTransactionsReportTest() {
        final ResponseEntity<List<Report>> response = transactionController.readUserTransactionsReport(USER_ID);
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(response.getBody(), LIST_REPORT);
    }

    @Test
    public void readRandomTransactionTest() {
        final ResponseEntity<TransactionDTO> response = transactionController.readRandomTransaction();
        assertEquals(response.getStatusCode(), HttpStatus.OK);
        assertEquals(response.getStatusCodeValue(), HttpStatus.OK.value());
        assertEquals(response.getBody(), TRANSACTION_DTO);
    }
}
