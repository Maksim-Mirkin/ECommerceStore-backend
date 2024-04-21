package edu.mlm.ecommercestore.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * Data Transfer Object for conveying a list of orders along with pagination details.
 * Includes total number of orders, page details, and flags for navigation.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for conveying a list of orders along with pagination details." +
        " Includes total number of orders, page details, and flags for navigation." +
        " Useful for displaying paginated orders.")
public class OrderListDTO {

    /**
     * Total number of orders across all pages.
     */
    @Schema(description = "Total number of orders across all pages.", example = "100")
    private long totalOrders;

    /**
     * The current page number (zero-based).
     */
    @Schema(description = "The current page number (zero-based).", example = "1")
    private int pageNumber;

    /**
     * The size of the page - the number of orders per page.
     */
    @Schema(description = "The size of the page - the number of orders per page.", example = "10")
    private int pageSize;

    /**
     * Total number of available pages.
     */
    @Schema(description = "Total number of available pages.", example = "10")
    private int totalPages;

    /**
     * Flag indicating whether the current page is the first one.
     */
    @Schema(description = "Flag indicating whether the current page is the first one.", example = "true")
    private boolean isFirst;

    /**
     * Flag indicating whether the current page is the last one.
     */
    @Schema(description = "Flag indicating whether the current page is the last one.", example = "false")
    private boolean isLast;

    /**
     * A collection of orders for the current page.
     */
    @Schema(description = "A collection of orders for the current page.")
    private Collection<OrderResponseDTO> orders;
}
