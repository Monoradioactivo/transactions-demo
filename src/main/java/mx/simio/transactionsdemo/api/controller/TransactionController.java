package mx.simio.transactionsdemo.api.controller;

import mx.simio.transactionsdemo.api.dto.TransactionDTO;
import mx.simio.transactionsdemo.api.model.Report;
import mx.simio.transactionsdemo.api.model.Sum;
import mx.simio.transactionsdemo.api.service.impl.TransactionServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

import static mx.simio.transactionsdemo.api.util.AppConstant.API_BASE_URI;

@RestController
@RequestMapping(API_BASE_URI)
public class TransactionController {
    private final TransactionServiceImpl transactionService;

    @Autowired
    public TransactionController(TransactionServiceImpl transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/users/{userId}/transactions")
    public ResponseEntity<TransactionDTO> createTransaction(@PathVariable Long userId,
                                                            @Valid @RequestBody TransactionDTO transactionDTO) {
        return new ResponseEntity<>(transactionService.createTransaction(userId, transactionDTO), HttpStatus.CREATED);
    }

    @GetMapping("/users/{userId}/transactions")
    public ResponseEntity<List<TransactionDTO>> readUserTransactions(@PathVariable Long userId) {
        return new ResponseEntity<>(transactionService.readUserTransactions(userId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/transactions/sum")
    public ResponseEntity<Sum> sumUserTransactions(@PathVariable Long userId) {
        return new ResponseEntity<>(transactionService.sumUserTransactions(userId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/transactions/{transactionId}")
    public ResponseEntity<TransactionDTO> readUserTransaction(@PathVariable Long userId,
                                                              @PathVariable String transactionId) {
        return new ResponseEntity<>(transactionService.readUserTransaction(userId, transactionId), HttpStatus.OK);
    }

    @GetMapping("/users/{userId}/transactions/reports")
    public ResponseEntity<List<Report>> readUserTransactionsReport(@PathVariable Long userId) {
        return new ResponseEntity<>(transactionService.readUserTransactionsReport(userId), HttpStatus.OK);
    }

    @GetMapping("/transactions/random")
    public ResponseEntity<TransactionDTO> readRandomTransaction() {
        return new ResponseEntity<>(transactionService.readRandomTransaction(), HttpStatus.OK);
    }
}
