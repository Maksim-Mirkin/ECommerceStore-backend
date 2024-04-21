package edu.mlm.ecommercestore.dto.order;

import edu.mlm.ecommercestore.dto.user.UserResponseDTO;
import edu.mlm.ecommercestore.enums.Status;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Data Transfer Object for returning order details in responses.
 * This DTO includes comprehensive information about an order, such as items, customer details, order status, and more.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for returning order details in responses." +
        " Includes comprehensive information about an order, such as items, customer details," +
        " order status, and more.")
public class OrderResponseDTO {

    /**
     * The unique identifier of the order.
     */
    @Schema(description = "The unique identifier of the order.", example = "1")
    private Long id;

    /**
     * List of items within the order.
     */
    @Schema(description = "List of items within the order.")
    private List<OrderItemResponseDTO> items;

    /**
     * Details of the customer who placed the order.
     */
    @Schema(
            description = "Details of the customer who placed the order.",
            implementation = UserResponseDTO.class
    )
    private UserResponseDTO customer;

    /**
     * The date and time when the order was placed.
     */
    @Schema(description = "The date and time when the order was placed.", example = "2023-01-01T12:00:00")
    private LocalDateTime createdAt;


    /**
     * The date and time when the order was last updated.
     * This field captures any modifications made to the order after its initial creation.
     */
    @Schema(description = "The date and time when the order was last updated.", example = "2023-01-02T15:30:00")
    private LocalDateTime updatedAt;

    /**
     * The total price of the order.
     */
    @Schema(description = "The total price of the order.", example = "150.00")
    private BigDecimal totalPrice;

    /**
     * The delivery address for the order.
     */
    @Schema(description = "The delivery address for the order.", example = "123 Main Street")
    private String address;

    /**
     * The city of the delivery address.
     */
    @Schema(description = "The city of the delivery address.", example = "Anytown")
    private String city;

    /**
     * Payment information or method used for the order.
     */
    @Schema(description = "Payment information or method used for the order.", example = "Credit Card")
    private String paymentInformation;

    /**
     * The postal code of the delivery address.
     */
    @Schema(description = "The postal code of the delivery address.", example = "1234567")
    private String postalCode;

    /**
     * The phone number associated with the order.
     */
    @Schema(description = "The phone number associated with the order.", example = "0501112233")
    private String phoneNumber;

    /**
     * The status of the order.
     */
    @Schema(description = "The status of the order.", example = "PENDING/APPROVED/DECLINED")
    private Status status;
}
