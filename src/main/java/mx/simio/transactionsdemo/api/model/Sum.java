package mx.simio.transactionsdemo.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"userId", "sum"})
public class Sum {
    @JsonProperty("user_id")
    private Long userId;
    private BigDecimal sum;

    @Builder
    public Sum(Long userId, BigDecimal sum) {
        this.userId = userId;
        this.sum = sum;
    }
}
