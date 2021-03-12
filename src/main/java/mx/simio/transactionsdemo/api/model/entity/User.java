package mx.simio.transactionsdemo.api.model.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @OneToMany(
            mappedBy = "user",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Transaction> transactions = new ArrayList<>();

    public User(String name) {
        this.name = name;
    }

    public void addTransaction(Transaction transaction) {
        transactions.add(transaction);
        transaction.setUser(this);
    }

    private static final long serialVersionUID = 9155418662253899928L;
}
