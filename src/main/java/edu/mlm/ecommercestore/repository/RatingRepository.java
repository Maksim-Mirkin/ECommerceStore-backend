package edu.mlm.ecommercestore.repository;

import edu.mlm.ecommercestore.entity.Product;
import edu.mlm.ecommercestore.entity.Rating;
import edu.mlm.ecommercestore.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Spring Data JPA repository for {@link Rating} entities.
 * <p>
 * Handles CRUD operations and additional queries for {@link Rating} entities within the e-commerce system.
 * By extending {@link JpaRepository}, it provides standard database interaction functionalities and allows for
 * the definition of custom queries to suit specific requirements.
 *
 * @see JpaRepository
 * @see Rating
 */
public interface RatingRepository extends JpaRepository<Rating, Long> {

    /**
     * Finds a rating given by a specific user for a specific product.
     * <p>
     * This method facilitates the retrieval of a unique rating based on the user who provided it and the product
     * it relates to. It's particularly useful for operations that involve checking or updating a user's rating for a product,
     * ensuring that each product-user combination has at most one associated rating.
     *
     * @param product the {@link Product} for which the rating is to be found
     * @param user the {@link User} who provided the rating
     * @return an {@link Optional} containing the found {@link Rating} if available; otherwise, an empty {@link Optional}
     */
    Optional<Rating> findByProductAndUser(Product product, User user);
}

