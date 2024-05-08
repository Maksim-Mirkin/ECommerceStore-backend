package edu.mlm.ecommercestore.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.validation.annotation.Validated;

import java.util.List;


/**
 * Data Transfer Object for order placement requests.
 * Includes details about the items being ordered, delivery address, contact information, and payment details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Validated
@Schema(description = "Data Transfer Object for order placement requests." +
        " Includes details about the items being ordered, delivery address, contact information," +
        " and payment details.")
public class OrderRequestDTO {

    /**
     * List of items requested in the order. Each item includes product details and the quantity ordered.
     */
    @NotNull
    @Schema(description = "List of items requested in the order, including product details and quantity.")
    private List<@Valid OrderItemRequestDTO> orderItems;

    /**
     * Delivery address for the order. Must be between 2 and 50 characters.
     */
    @NotNull
    @Size(min = 2, max = 50, message = "Address must be between 2 and 50 characters")
    @Schema(
            description = "Delivery address for the order. Must be between 2 and 50 characters.",
            example = "123 Main St"
    )
    private String address;

    /**
     * City for the delivery address. Must be between 1 and 187 characters.
     */
    @NotNull
    @Size(min = 1, max = 187, message = "City must be between 1 and 187 characters")
    @Schema(
            description = "City for the delivery address. Must be between 1 and 187 characters.",
            example = "Metropolis"
    )
    private String city;

    /**
     * Contact phone number for the order. Must be 10 digits without country code.
     */
    @NotNull
    @Pattern(regexp = "^[0-9]{10}$",
            message = "Invalid phone number, must be 10 digits without country code")
    @Schema(
            description = "Contact phone number for the order. Must be 10 digits without country code.",
            example = "0501112233"
    )
    private String phoneNumber;

    /**
     * Payment information or method for the order.
     */
    @NotNull
    @Schema(description = "Payment information or method for the order.", example = "Credit Card")
    private String paymentInformation;

    /**
     * Postal code for the delivery address. Must be 7 digits.
     */
    @NotNull
    @Pattern(regexp = "^[0-9]{7}$", message = "Invalid postal code, must be 7 digits")
    @Schema(description = "Postal code for the delivery address. Must be 7 digits.", example = "1234567")
    private String postalCode;
}
