package edu.mlm.ecommercestore.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for requesting updates or queries on the status of an order.
 * This DTO allows specifying the status to set for an order or to query orders by their status.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for requesting updates or queries on the status of an order." +
        " Allows specifying the status to set for an order or to query orders by their status." +
        " The status should align with predefined order status values.")
public class OrderStatusRequestDTO {

    /**
     * The status to be updated or queried in the order.
     * This should match predefined order status values.
     */
    @NotNull
    @Schema(description = "The status to be updated or queried in the order. Must be a valid order status value.",
            example = "PENDING/APPROVED/DECLINED")
    private String status;
}
