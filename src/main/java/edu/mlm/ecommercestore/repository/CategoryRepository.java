package edu.mlm.ecommercestore.repository;

import edu.mlm.ecommercestore.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Category} entities.
 * <p>
 * This interface provides the mechanism for storage, retrieval, update, and delete operations on {@link Category} entities.
 * <p>
 * Usage of this repository allows for the abstraction of data access complexities and supports the integration
 * with Spring Data JPA to facilitate the implementation of repository layers.
 *
 * @see JpaRepository
 * @see Category
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {

    /**
     * Finds a {@link Category} by its name, ignoring case.
     * <p>
     * This method provides a case-insensitive search functionality to locate a {@link Category} based on its name.
     * This is particularly useful in scenarios where the exact case of category names might vary or is unknown.
     *
     * @param category the name of the category to find
     * @return an {@link Optional} containing the found {@link Category} if available; otherwise, an empty {@link Optional}
     */
    Optional<Category> findCategoryByNameIgnoreCase(String category);
}
