package mx.simio.transactionsdemo.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"id", "amount", "description", "date", "userId"})
public class TransactionDTO {
    @JsonProperty("transaction_id")
    private UUID id;
    @NotNull(message = "Amount must not be null")
    private BigDecimal amount;
    @NotNull(message = "Description must not be null")
    @NotBlank(message = "Description must not be blank")
    @NotEmpty(message = "Description must not be empty")
    private String description;
    private String date;
    @JsonProperty("user_id")
    private Long userId;

    @Builder
    public TransactionDTO(UUID id, BigDecimal amount, String description, String date, Long userId) {
        this.id = id;
        this.amount = amount;
        this.description = description;
        this.date = date;
        this.userId = userId;
    }
}
