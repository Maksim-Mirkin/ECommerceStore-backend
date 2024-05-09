package edu.mlm.ecommercestore.controller;

import edu.mlm.ecommercestore.dto.exception.ExceptionDTO;
import edu.mlm.ecommercestore.dto.exception.InternalServerExceptionDTO;
import edu.mlm.ecommercestore.dto.order.OrderListDTO;
import edu.mlm.ecommercestore.dto.order.OrderRequestDTO;
import edu.mlm.ecommercestore.dto.order.OrderResponseDTO;
import edu.mlm.ecommercestore.dto.order.OrderStatusRequestDTO;
import edu.mlm.ecommercestore.service.order.OrderService;
import edu.mlm.ecommercestore.validator.AllowedParameters;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Controller for managing orders within the application.
 * Provides endpoints for creating, retrieving, and updating orders.
 * It includes functionality for both customers and administrators,
 * with specific operations requiring admin privileges.
 *
 * @author Maksim Mirkin
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/orders")
@Tag(name = "Order", description = "Order API")
public class OrderController {

    private final OrderService orderService;

    /**
     * Creates a new order based on the provided order details.
     * Authorization is required to create an order.
     *
     * @param dto The order request DTO containing order details.
     * @param authentication The authentication token of the user creating the order.
     * @param uriBuilder A utility to help build the URI for the newly created resource.
     * @return {@link OrderResponseDTO} containing the order response DTO.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order created successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid order request input",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "You are not authorized to create an order",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Products not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerExceptionDTO.class)
                    )
            )
    })
    @Operation(summary = "Create Order",
            description = "Creates a new order with the provided order details." +
                    " Requires user authentication.")
    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(
            @RequestBody @Valid OrderRequestDTO dto,
            Authentication authentication,
            UriComponentsBuilder uriBuilder
    ) {
        val res = orderService.createOrder(dto, authentication);
        val uri = uriBuilder.path("/api/v1/orders/{id}").buildAndExpand(res.getId()).toUri();
        return ResponseEntity.created(uri).body(res);
    }

    /**
     * Retrieves orders for the currently authenticated customer.
     * Supports pagination and sorting by parameters.
     *
     * @param authentication The authentication token of the requesting user.
     * @param pageNumber The page number for pagination.
     * @param pageSize The number of items per page.
     * @param sortDir The direction of the sort (e.g., asc or desc).
     * @param sortBy The property to sort by.
     * @return {@link OrderListDTO} containing a list of {@link OrderResponseDTO}.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid page number/size/sort direction properties",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "You are not authorized to view orders",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Orders not found/orders property not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerExceptionDTO.class)
                    )
            )
    })
    @Operation(summary = "Get Orders by Customer",
            description = "Retrieves orders for the currently authenticated customer" +
                    " with support for pagination and sorting.")
    @AllowedParameters({"pageNumber", "pageSize", "sortDir", "sortBy"})
    @GetMapping
    public ResponseEntity<OrderListDTO> getOrdersByCustomer(
            Authentication authentication,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "5") int pageSize,
            @RequestParam(value = "sortDir", required = false, defaultValue = "desc") String sortDir,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy) {
        return ResponseEntity.ok(orderService.getOrdersByCustomer(authentication, pageNumber, pageSize, sortDir, sortBy));
    }

    /**
     * Retrieves all orders within the system.
     * This operation requires admin privileges and supports pagination and sorting.
     *
     * @param authentication The authentication token of the requesting admin.
     * @param pageNumber The page number for pagination.
     * @param pageSize The number of items per page.
     * @param sortDir The direction of the sort (e.g., asc or desc).
     * @param sortBy The property to sort by.
     * @return {@link OrderListDTO} containing a list of {@link OrderResponseDTO}.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Orders retrieved successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid page number/page size/sort direction/sort by property",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "You are not authorized to view orders",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "You have no permission for this request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Orders not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerExceptionDTO.class)
                    )
            )
    })
    @Operation(summary = "Get All Orders",
            description = "Retrieves all orders within the system for admins" +
                    " with support for pagination and sorting.")
    @AllowedParameters({"pageNumber", "pageSize", "sortDir", "sortBy"})
    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderListDTO> getAllOrders(
            Authentication authentication,
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "10") int pageSize,
            @RequestParam(value = "sortDir", required = false, defaultValue = "desc") String sortDir,
            @RequestParam(value = "sortBy", required = false, defaultValue = "createdAt") String sortBy
    ) {
        return ResponseEntity.ok(orderService.getAllOrders(authentication, pageNumber, pageSize, sortDir, sortBy));
    }

    /**
     * Updates the status of an existing order.
     * This operation is restricted to admin users.
     *
     * @param id The unique identifier of the order to update.
     * @param dto The order status request DTO containing the new status.
     * @param authentication The authentication token of the admin making the request.
     * @return {@link OrderResponseDTO} containing the updated order response DTO.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order status updated successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid order status request input",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "You are not authorized to update order status",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "You have no permission for this request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Order not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerExceptionDTO.class)
                    )
            )
    })
    @Operation(summary = "Update Order Status",
            description = "Updates the status of an existing order. Restricted to admin users.")
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<OrderResponseDTO> updateOrderStatus(
            @PathVariable long id,
            @RequestBody @Valid OrderStatusRequestDTO dto,
            Authentication authentication
    ) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, dto, authentication));
    }
}
