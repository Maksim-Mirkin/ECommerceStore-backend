package edu.mlm.ecommercestore.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

/**
 * Data Transfer Object for conveying a paginated list of products.
 * Includes pagination details such as total number of products, page number, and size, as well as navigation flags.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for conveying a paginated list of products." +
        " Includes pagination details such as total number of products," +
        " page number, size, and navigation flags.")
public class ProductListDTO {

    /**
     * The total number of products across all pages.
     */
    @Schema(description = "The total number of products across all pages.", example = "100")
    private long totalProducts;

    /**
     * The current page number (zero-based).
     */
    @Schema(description = "The current page number (zero-based).", example = "1")
    private int pageNumber;

    /**
     * The number of products per page.
     */
    @Schema(description = "The number of products per page.", example = "10")
    private int pageSize;

    /**
     * The total number of pages available.
     */
    @Schema(description = "The total number of pages available.", example = "10")
    private int totalPages;

    /**
     * Indicates whether the current page is the first one.
     */
    @Schema(description = "Indicates whether the current page is the first one.", example = "true")
    private boolean isFirst;

    /**
     * Indicates whether the current page is the last one.
     */
    @Schema(description = "Indicates whether the current page is the last one.", example = "false")
    private boolean isLast;

    /**
     * A collection of products for the current page.
     */
    @Schema(description = "A collection of products for the current page.")
    private Collection<ProductResponseDTO> products;
}
