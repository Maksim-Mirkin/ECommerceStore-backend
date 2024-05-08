package edu.mlm.ecommercestore.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Data Transfer Object for product creation or update requests.
 * Includes comprehensive product details such as name, price, brand, and more.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for product creation or update requests." +
        " Includes comprehensive product details such as name, price, brand, description," +
        " image URL, category, and specifications like memory, weight, battery capacity," +
        " operating system, and color.")
public class ProductRequestDTO {

    /**
     * The name of the product.
     */
    @NotNull
    @Size(min = 2, max = 30)
    @Schema(description = "The name of the product.", example = "IPhone X")
    private String name;

    /**
     * The price of the product.
     */
    @NotNull
    @DecimalMin("1.0")
    @Schema(description = "The price of the product.", example = "499.99")
    private BigDecimal price;

    /**
     * The brand of the product.
     */
    @NotNull
    @Size(min = 2, max = 30)
    @Schema(description = "The brand of the product.", example = "Apple")
    private String brand;

    /**
     * A detailed description of the product.
     */
    @NotNull
    @Size(min = 2, max = 512)
    @Schema(
            description = "A detailed description of the product.",
            example = "Latest model with advanced features."
    )
    private String description;

    /**
     * A URL or a reference to the product image.
     */
    @NotEmpty
    @Schema(
            description = "A URL or a reference to the product image.",
            example = "http://example.com/image.jpg"
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
     * The weight of the product.
     */
    @Schema(description = "The weight of the product in kilograms.", example = "0.5")
    private BigDecimal weight;

    /**
     * The battery capacity of the product, if applicable.
     */
    @Schema(description = "The battery capacity of the product, if applicable.", example = "4000mAh")
    private String batteryCapacity;

    /**
     * The operating system of the product, if applicable.
     */
    @Schema(description = "The operating system of the product, if applicable.", example = "iOS")
    private String operatingSystem;

    /**
     * The color of the product.
     */
    @Schema(description = "The color of the product.", example = "Black")
    private String color;
}
