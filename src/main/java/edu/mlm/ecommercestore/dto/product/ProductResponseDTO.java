package edu.mlm.ecommercestore.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Data Transfer Object for conveying product details in responses.
 * Includes all relevant information about a product, such as its specifications, ratings, and timestamps for creation and last update.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for conveying product details in responses." +
        " Includes comprehensive information about a product, such as its specifications," +
        " ratings, and timestamps for creation and last update.")
public class ProductResponseDTO {

    /**
     * The unique identifier of the product.
     */
    @Schema(description = "The unique identifier of the product.", example = "1")
    private long id;

    /**
     * The name of the product.
     */
    @Schema(description = "The name of the product.", example = "Smartphone X")
    private String name;

    /**
     * The price of the product.
     */
    @Schema(description = "The price of the product.", example = "499.99")
    private BigDecimal price;

    /**
     * The brand of the product.
     */
    @Schema(description = "The brand of the product.", example = "TechBrand")
    private String brand;

    /**
     * A detailed description of the product.
     */
    @Schema(
            description = "A detailed description of the product.",
            example = "Latest model with advanced features."
    )
    private String description;

    /**
     * A URL or a reference to the product image.
     */
    @Schema(
            description = "A URL or a reference to the product image.",
            example = "https://example.com/image.jpg"
    )
    private String image;

    /**
     * The category the product belongs to.
     */
    @NotNull
    @Schema(description = "The category the product belongs to.", example = "Cellular/TV/Headphone/Laptop")
    private String category;

    /**
     * The memory specification of the product, if applicable.
     */
    @Schema(description = "The memory specification of the product, if applicable.", example = "8GB")
    private String memory;

    /**
     * The screen size of the product in inches.
     */
    @Schema(description = "The screen size of the product in inches.", example = "3\"")
    private String screenSize;

    /**
     * The battery capacity of the product, if applicable.
     */
    @Schema(description = "The battery capacity of the product, if applicable.", example = "4000mAh")
    private String batteryCapacity;

    /**
     * The operating system of the product, if applicable.
     */
    @Schema(description = "The operating system of the product, if applicable.", example = "Android 11")
    private String operatingSystem;

    /**
     * The color of the product.
     */
    @Schema(description = "The color of the product.", example = "Black")
    private String color;

    /**
     * The average rating of the product based on customer reviews.
     */
    @Schema(
            description = "The average rating of the product based on customer reviews.",
            example = "4.5"
    )
    private double averageRating;

    /**
     * The timestamp when the product was created.
     */
    @Schema(description = "The timestamp when the product was created.", example = "2023-01-01T12:00:00")
    private LocalDateTime createdAt;

    /**
     * The timestamp when the product details were last updated.
     */
    @Schema(
            description = "The timestamp when the product details were last updated.",
            example = "2023-01-02T12:00:00"
    )
    private LocalDateTime updatedAt;
}
