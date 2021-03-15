package mx.simio.transactionsdemo.api.model.entity;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@Table(name = "transactions")
public class Transaction implements Serializable {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;
    private BigDecimal amount;
    private String description;
    private LocalDate date;
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    public Transaction(BigDecimal amount, String description, LocalDate date) {
        this.amount = amount;
        this.description = description;
        this.date = date;
    }

    @Builder
    public Transaction(BigDecimal amount, String description, LocalDate date, User user) {
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.user = user;
    }

    public static void sortTransactionsChronologically(List<Transaction> transactions) {
        transactions.sort(Comparator.comparing(Transaction::getDate));
    }

    @Override
    public String toString() {
        return "Transaction{}";
    }

    private static final long serialVersionUID = 8362215857577388142L;
}
