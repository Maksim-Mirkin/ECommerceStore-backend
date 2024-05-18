package edu.mlm.ecommercestore.repository;

import edu.mlm.ecommercestore.entity.Product;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;


/**
 * Spring Data JPA repository for {@link Product} entities.
 * <p>
 * This interface provides the mechanism for basic CRUD operations and query generation for {@link Product} entities
 * through the extension of {@link JpaRepository}. It leverages Spring Data JPA to abstract the boilerplate data access
 * code, allowing for simplified interaction with the database layer. It includes custom repository methods beyond the
 * standard CRUD operations provided by {@link JpaRepository}.
 * </p>
 *
 * @see JpaRepository
 * @see Product
 */
public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    /**
     * Checks whether a {@link Product} with the specified ID exists in the database.
     *
     * @param id The ID of the {@link Product} to check for existence. Can be {@code null}.
     * @return {@code true} if a {@link Product} with the specified ID exists, {@code false} otherwise.
     */
    boolean existsById(@Nullable Long id);

    @Query("SELECT p FROM Product p LEFT JOIN p.ratings r GROUP BY p.id ORDER BY AVG(r.rating) DESC")
    Page<Product> findAllOrderByAverageRatingDesc(Pageable pageable);

    @Query("SELECT p FROM Product p LEFT JOIN p.ratings r GROUP BY p.id ORDER BY AVG(r.rating) ASC")
    Page<Product> findAllOrderByAverageRatingAsc(Pageable pageable);
}