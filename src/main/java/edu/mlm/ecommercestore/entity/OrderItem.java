package edu.mlm.ecommercestore.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "order_items")
@Schema(description = "Represents an item within an order in the e-commerce system, including its quantity and subtotal.")
public class OrderItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the order item")
    private Long id;

    @NotNull
    @Min(1)
    @Schema(description = "The quantity of the product ordered", example = "2")
    private int quantity;

    @NotNull
    @Schema(
            description = "The subtotal price for this item, calculated as the product price times quantity",
            example = "200.00"
    )
    private BigDecimal subTotal;

    @ManyToOne
    @JoinColumn(nullable = false, name = "product_id")
    @Schema(description = "The product that this order item represents")
    private Product product;

    @ManyToOne
    @JoinColumn(nullable = false, name = "order_id")
    @Schema(description = "The order to which this item belongs")
    private Order order;
}