package edu.mlm.ecommercestore.repository;

import edu.mlm.ecommercestore.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Spring Data JPA repository for {@link Order} entities.
 * <p>
 * Facilitates CRUD operations and additional querying capabilities for {@link Order} entities within the e-commerce system.
 * This repository interface extends {@link JpaRepository}, leveraging Spring Data's repository support to simplify
 * data access patterns. It provides automatic implementation of database interactions for orders, including but not
 * limited to creation, retrieval, update, and deletion.
 *
 * @see JpaRepository
 * @see Order
 */
public interface OrderRepository extends JpaRepository<Order, Long> {

    /**
     * Retrieves a paginated list of orders associated with a specific customer ID.
     * <p>
     * This method supports the dynamic query of orders based on the customer's ID, allowing for scalable and
     * efficient data retrieval through pagination. It is particularly useful in managing and displaying user orders
     * in systems with a significant number of transactions or users.
     *
     * @param customerId the ID of the customer whose orders are to be retrieved
     * @param pageable a {@link Pageable} instance containing pagination information (e.g., page number, page size)
     * @return a {@link Page} of {@link Order} entities matching the given customer ID, adhering to the pagination request
     */
    Page<Order> findByCustomerId(Long customerId, Pageable pageable);
}