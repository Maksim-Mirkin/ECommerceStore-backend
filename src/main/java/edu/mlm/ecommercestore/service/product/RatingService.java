package edu.mlm.ecommercestore.service.product;

import edu.mlm.ecommercestore.dto.rating.RatingRequestDTO;
import edu.mlm.ecommercestore.dto.rating.RatingResponseDTO;
import org.springframework.security.core.Authentication;

/**
 * Service interface for managing ratings of products.
 * <p>
 * Defines the contract for services that handle posting, updating, and deleting ratings for products. This interface
 * abstracts the business logic associated with managing ratings, allowing for implementations that handle the specifics
 * of rating persistence and validation. Ensures that user ratings are correctly associated with products and users,
 * and maintains the integrity and relevance of product feedback.
 */
public interface RatingService {

    /**
     * Posts a new rating for a product by a user.
     * <p>
     * Handles the creation of a new rating for a specific product based on the authenticated user's input.
     * Validates the provided rating details and associates the rating with the specified product and the
     * authenticated user.
     *
     * @param productId the ID of the product to rate
     * @param dto the data transfer object containing the rating information
     * @param authentication the authentication context of the user submitting the rating
     * @return a {@link RatingResponseDTO} containing the details of the newly posted rating
     */
    RatingResponseDTO postRating(long productId, RatingRequestDTO dto, Authentication authentication);

    /**
     * Updates an existing rating for a product.
     * <p>
     * Allows a user to modify their previously submitted rating for a product. Ensures that only the user who
     * originally posted the rating can update it, maintaining the authenticity of user feedback.
     *
     * @param productId the ID of the product whose rating is to be updated
     * @param dto the data transfer object containing the updated rating information
     * @param authentication the authentication context of the user updating the rating
     * @return a {@link RatingResponseDTO} reflecting the updated rating details
     */
    RatingResponseDTO updateRating(long productId, RatingRequestDTO dto, Authentication authentication);
}
