package edu.mlm.ecommercestore.service.order;

import edu.mlm.ecommercestore.dto.order.OrderListDTO;
import edu.mlm.ecommercestore.dto.order.OrderRequestDTO;
import edu.mlm.ecommercestore.dto.order.OrderResponseDTO;
import edu.mlm.ecommercestore.dto.order.OrderStatusRequestDTO;
import edu.mlm.ecommercestore.entity.Order;
import edu.mlm.ecommercestore.entity.OrderItem;
import edu.mlm.ecommercestore.entity.User;
import edu.mlm.ecommercestore.enums.Status;
import edu.mlm.ecommercestore.error.InvalidPropertyException;
import edu.mlm.ecommercestore.error.PaginationException;
import edu.mlm.ecommercestore.error.ResourceNotFoundException;
import edu.mlm.ecommercestore.repository.OrderRepository;
import edu.mlm.ecommercestore.repository.UserRepository;
import edu.mlm.ecommercestore.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import edu.mlm.ecommercestore.error.AuthenticationException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the {@link OrderService} for managing orders within the e-commerce platform.
 * <p>
 * Handles the creation, retrieval, and updating of orders by interacting with the application's persistence layer.
 * Utilizes {@link ModelMapper} for object mapping, ensuring seamless conversion between entities and DTOs.
 * This service also incorporates business logic for order processing, including validation checks and status updates.
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final ProductService productService;


    /**
     * {@inheritDoc}
     */
    @Override
    public OrderResponseDTO createOrder(OrderRequestDTO dto, Authentication authentication) {
        val user = getUserEntityOrThrow(authentication);

        List<OrderItem> orderItems = new ArrayList<>();
        dto.getOrderItems()
                .forEach(orderItem -> {
                    var item = OrderItem.builder()
                            .product(productService.getProductEntityOrThrow(orderItem.getProductId()))
                            .quantity(orderItem.getQuantity())
                            .build();
                    orderItems.add(item);
                });

        var order = modelMapper.map(dto, Order.class);
        var totalAmount = BigDecimal.ZERO;

        for (val item : orderItems) {
            val price = item.getProduct().getPrice();
            val quantity = BigDecimal.valueOf(item.getQuantity());
            val subTotal = price.multiply(quantity);
            totalAmount = totalAmount.add(subTotal);
            item.setSubTotal(subTotal);
            item.setOrder(order);
        }

        order.setCustomer(user);
        order.setTotalPrice(totalAmount);
        val pending = Status.PENDING.name();
        order.setStatus(pending);
        order.setItems(orderItems);

        val saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderResponseDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderListDTO getOrdersByCustomer(
            Authentication authentication,
            int pageNumber,
            int pageSize,
            String sortDir,
            String... sortBy
    ) {
        val user = getUserEntityOrThrow(authentication);
        try {
            Sort.Direction sort = Sort.Direction.fromString(sortDir);
            val pageable = PageRequest.of(pageNumber, pageSize, sort, sortBy);
            val pr = orderRepository.findByCustomerId(user.getId(), pageable);

            return getOrderListDTO(pageNumber, pr);
        } catch (IllegalArgumentException e) {
            throw new PaginationException(e.getMessage());
        } catch (PropertyReferenceException e) {
            throw new InvalidPropertyException(e.getPropertyName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderListDTO getAllOrders(
            Authentication authentication,
            int pageNumber,
            int pageSize,
            String sortDir,
            String... sortBy
    ) {
        validateAdminOrThrow(authentication);
        try {
            Sort.Direction sort = Sort.Direction.fromString(sortDir);
            val pageable = PageRequest.of(pageNumber, pageSize, sort, sortBy);
            val pr = orderRepository.findAll(pageable);

            return getOrderListDTO(pageNumber, pr);
        } catch (IllegalArgumentException e) {
            throw new PaginationException(e.getMessage());
        } catch (PropertyReferenceException e) {
            throw new InvalidPropertyException(e.getPropertyName());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public OrderResponseDTO updateOrderStatus(
            Long orderId,
            OrderStatusRequestDTO dto,
            Authentication authentication
    ) {
        validateAdminOrThrow(authentication);
        val order = orderRepository.findById(orderId).orElseThrow(
                ResourceNotFoundException.newInstance("Order", "id", orderId)
        );
        order.setStatus(dto.getStatus());
        val saved = orderRepository.save(order);
        return modelMapper.map(saved, OrderResponseDTO.class);
    }

    /**
     * Retrieves a paginated list DTO of orders.
     *
     * @param pageNumber The page number of the requested page.
     * @param pr         The page of {@link Order} entities.
     * @return The {@link OrderListDTO} with the paginated order data.
     */
    private OrderListDTO getOrderListDTO(int pageNumber, Page<Order> pr) {
        if (pageNumber >= pr.getTotalPages()) {
            throw new PaginationException("Page number " + pageNumber + " exceeds total pages " + pr.getTotalPages());
        }

        List<OrderResponseDTO> orderListDTO =
                pr.getContent().stream()
                        .map(o -> modelMapper.map(o, OrderResponseDTO.class))
                        .toList();

        return new OrderListDTO(
                pr.getTotalElements(),
                pr.getNumber(),
                pr.getSize(),
                pr.getTotalPages(),
                pr.isFirst(),
                pr.isLast(),
                orderListDTO
        );
    }

    /**
     * Retrieves a {@link User} entity based on the current authentication context or throws an exception.
     *
     * @param authentication The current authentication context.
     * @return The {@link User} entity.
     */
    private User getUserEntityOrThrow(Authentication authentication) {
        val user = userRepository.findUserByUsernameIgnoreCase(authentication.getName());
        return user.orElseThrow(AuthenticationException::new);
    }

    /**
     * Validates if the authenticated user has admin privileges or throws an exception.
     *
     * @param authentication The current authentication context.
     */
    private void validateAdminOrThrow(Authentication authentication) {
        val user = getUserEntityOrThrow(authentication);
        val isAdmin = user
                .getRoles().stream()
                .anyMatch(r -> r.getRoleName().equalsIgnoreCase("ROLE_ADMIN")
                );
        if (!isAdmin) {
            throw new AuthenticationException(user.getUsername() + "has no permission for this action");
        }
    }
}
