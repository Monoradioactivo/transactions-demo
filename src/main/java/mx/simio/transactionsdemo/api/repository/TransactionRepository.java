package mx.simio.transactionsdemo.api.repository;

import mx.simio.transactionsdemo.api.model.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, UUID> {
    List<Transaction> findByUserId(Long userId);
    Optional<Transaction> findByUserIdAndId(Long userId, UUID transactionId);
}
