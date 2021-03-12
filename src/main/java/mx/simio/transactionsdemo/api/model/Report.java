package mx.simio.transactionsdemo.api.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@JsonPropertyOrder({"userId", "weekStartDate", "weekFinishDate", "quantity"})
public class Report {
    @JsonProperty("user_id")
    private Long userId;
    @JsonProperty("week_start_date")
    private String weekStartDate;
    @JsonProperty("week_finish_date")
    private String weekFinishDate;
    private Integer quantity;

    @Builder
    public Report(Long userId, String weekStartDate, String weekFinishDate, Integer quantity) {
        this.userId = userId;
        this.weekStartDate = weekStartDate;
        this.weekFinishDate = weekFinishDate;
        this.quantity = quantity;
    }
}
