package edu.mlm.ecommercestore.service.order;

import edu.mlm.ecommercestore.dto.order.OrderListDTO;
import edu.mlm.ecommercestore.dto.order.OrderRequestDTO;
import edu.mlm.ecommercestore.dto.order.OrderResponseDTO;
import edu.mlm.ecommercestore.dto.order.OrderStatusRequestDTO;
import org.springframework.security.core.Authentication;

/**
 * Service interface for managing orders within the e-commerce platform.
 * <p>
 * Defines the operations required for creating, retrieving, and updating orders.
 * This interface abstracts the business logic associated with order processing, allowing for
 * implementations that handle the specifics of order management, including security considerations
 * and integration with the underlying data store.
 */
public interface OrderService {

    /**
     * Creates a new order based on the provided order request data and the authenticated user's details.
     * <p>
     * This method handles the processing of new orders, ensuring that they are associated with the correct
     * customer (as determined by the provided {@link Authentication}) and that the order details are
     * validated and persisted.
     *
     * @param dto the order request data transfer object containing order details
     * @param authentication the authentication context of the user creating the order
     * @return an {@link OrderResponseDTO} containing the details of the newly created order
     */
    OrderResponseDTO createOrder(OrderRequestDTO dto, Authentication authentication);

    /**
     * Retrieves a paginated list of orders placed by the authenticated user.
     * <p>
     * This method allows customers to view their own orders, supporting pagination and sorting
     * to efficiently manage and display a potentially large number of orders.
     *
     * @param authentication the authentication context of the customer whose orders are being requested
     * @param pageNumber the page number for pagination
     * @param pageSize the number of orders per page
     * @param sortDir the direction of sorting (e.g., "asc" or "desc")
     * @param sortBy the properties to sort by
     * @return an {@link OrderListDTO} containing a paginated list of orders
     */
    OrderListDTO getOrdersByCustomer(
            Authentication authentication,
            int pageNumber,
            int pageSize,
            String sortDir,
            String... sortBy
    );

    /**
     * Retrieves a paginated list of all orders in the system for administrative purposes.
     * <p>
     * This method is intended for use by administrators to oversee all orders within the platform, with
     * support for pagination and sorting to handle large datasets.
     *
     * @param authentication the authentication context (expected to have administrative privileges)
     * @param pageNumber the page number for pagination
     * @param pageSize the number of orders per page
     * @param sortDir the direction of sorting
     * @param sortBy the properties to sort by
     * @return an {@link OrderListDTO} containing a paginated list of all orders
     */
    OrderListDTO getAllOrders(
            Authentication authentication,
            int pageNumber,
            int pageSize,
            String sortDir,
            String... sortBy
    );

    /**
     * Updates the status of an existing order, to mark it as approved, pending, or declined.
     * <p>
     * This method enables the modification of an order's status based on operational changes or completion
     * of certain stages in the order processing lifecycle. It is secured to ensure that only administrator
     * can update order statuses.
     *
     * @param orderId the ID of the order to update
     * @param dto the order status update request containing the new status
     * @param authentication the authentication context (for verifying permissions)
     * @return an {@link OrderResponseDTO} reflecting the updated order status
     */
    OrderResponseDTO updateOrderStatus(
            Long orderId,
            OrderStatusRequestDTO dto,
            Authentication authentication
    );
}

