package edu.mlm.ecommercestore.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for creating or updating order items.
 * It captures the necessary details for adding a product to an order, including the product ID and the quantity desired.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for creating or updating order items." +
        " Captures the product ID and the desired quantity for the order.")
public class OrderItemRequestDTO {

    /**
     * The ID of the product to be added to the order.
     * This should correspond to a valid product in the system.
     */
    @NotNull
    @Schema(description = "The ID of the product to be added to the order.", example = "1")
    private Long productId;

    /**
     * The quantity of the product to be added.
     * This must be at least 1, indicating the number of this product to be ordered.
     */
    @NotNull
    @Min(value = 1, message = "Quantity must be at least 1")
    @Schema(description = "The quantity of the product to be added to the order. Must be at least 1.",
            example = "2")
    private int quantity;
}
