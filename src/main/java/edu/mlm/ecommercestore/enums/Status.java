package edu.mlm.ecommercestore.enums;

import lombok.Getter;

/**
 * Represents the possible statuses of an orders within the application.
 * This enumeration defines the different states that an order, such as a request or an application,
 * can hold throughout its lifecycle within the system.
 * <p>
 * The statuses include:
 * <ul>
 *     <li>{@link #PENDING} - Indicates that the order is awaiting a decision or action.</li>
 *     <li>{@link #APPROVED} - Indicates that the order has been approved.</li>
 *     <li>{@link #DECLINED} - Indicates that the order has been declined.</li>
 * </ul>
 * Each status is associated with a {@code label} that represents the status as a {@link String}.
 */
@Getter
public enum Status {
    /**
     * Status indicating that the order is awaiting a decision or action.
     */
    PENDING("PENDING"),

    /**
     * Status indicating that the order has been approved.
     */
    APPROVED("APPROVED"),

    /**
     * Status indicating that the order has been declined.
     */
    DECLINED("DECLINED");

    /**
     * The label of the status, representing the status as a string.
     */
    private final String label;

    /**
     * Constructs a new {@code Status} enum constant with the specified label.
     *
     * @param label the label of the status, not null
     */
    Status(String label) {
        this.label = label;
    }
}