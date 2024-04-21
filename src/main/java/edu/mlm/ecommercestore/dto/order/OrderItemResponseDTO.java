package edu.mlm.ecommercestore.dto.order;

import edu.mlm.ecommercestore.dto.product.ProductResponseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for representing an order item in responses.
 * Provides details about each item within an order, including the product information, quantity, and subtotal cost.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for representing an order item in responses." +
        " Provides details about each item within an order, including the product information," +
        " quantity, and subtotal cost.")
public class OrderItemResponseDTO {

    /**
     * The unique identifier of the order item.
     */
    @Schema(description = "The unique identifier of the order item.", example = "1")
    private Long id;

    /**
     * Detailed information about the product associated with this order item.
     */
    @Schema(
            description = "Detailed information about the product associated with this order item.",
            implementation = ProductResponseDTO.class
    )
    private ProductResponseDTO product;

    /**
     * The quantity of the product ordered.
     */
    @Schema(description = "The quantity of the product ordered.", example = "2")
    private int quantity;

    /**
     * The subtotal for this order item, calculated as product price times quantity.
     */
    @Schema(
            description = "The subtotal for this order item, calculated as product price times quantity.",
            example = "100.00"
    )
    private BigDecimal subTotal;
}

