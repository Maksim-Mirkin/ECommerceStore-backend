package edu.mlm.ecommercestore.service.product;

import edu.mlm.ecommercestore.dto.rating.RatingRequestDTO;
import edu.mlm.ecommercestore.dto.rating.RatingResponseDTO;
import edu.mlm.ecommercestore.entity.Rating;
import edu.mlm.ecommercestore.entity.User;
import edu.mlm.ecommercestore.error.AuthenticationException;
import edu.mlm.ecommercestore.error.MultipleRatingException;
import edu.mlm.ecommercestore.error.ResourceNotFoundException;
import edu.mlm.ecommercestore.repository.RatingRepository;
import edu.mlm.ecommercestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RatingServiceImpl implements RatingService {

    private final RatingRepository ratingRepository;
    private final UserRepository userRepository;
    private final ProductService productService;
    private final ModelMapper modelMapper;

    /**
     * {@inheritDoc}
     */
    @Override
    public RatingResponseDTO postRating(long productId, RatingRequestDTO dto, Authentication authentication) {
        val product = productService.getProductEntityOrThrow(productId);
        val user = getUserEntityOrThrow(authentication);
        if (ratingRepository.findByProductAndUser(product, user).isPresent()) {
            throw new MultipleRatingException();
        }
        var rating = modelMapper.map(dto, Rating.class);
        rating.setUser(user);
        rating.setProduct(product);
        ratingRepository.save(rating);
        return modelMapper.map(rating, RatingResponseDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RatingResponseDTO updateRating(long productId, RatingRequestDTO dto, Authentication authentication) {
        val product = productService.getProductEntityOrThrow(productId);
        val user = getUserEntityOrThrow(authentication);
        val rating = ratingRepository.findByProductAndUser(product, user).orElseThrow(
                ResourceNotFoundException.newInstance("Rating", "username", user.getUsername())
        );
        val ratingBeforeSave = Rating.builder()
                .id(rating.getId())
                .user(user)
                .product(product)
                .rating(dto.getRating())
                .createdAt(rating.getCreatedAt())
                .build();
        val saved = ratingRepository.save(ratingBeforeSave);
        return modelMapper.map(saved, RatingResponseDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public RatingResponseDTO getRatingByProductId(long productId, Authentication authentication) {
        val product = productService.getProductEntityOrThrow(productId);
        val user = getUserEntityOrThrow(authentication);
        val rating = ratingRepository.findByProductAndUser(product, user).orElseThrow(
                ResourceNotFoundException.newInstance("Rating", "username", user.getUsername())
        );
        return modelMapper.map(rating, RatingResponseDTO.class);
    }

    /**
     * Retrieves a {@link User} entity based on the current authentication context, or throws an exception if not found.
     * <p>
     * This private helper method facilitates the retrieval of user entities, ensuring that operations such as
     * posting, updating, and deleting ratings are performed by authenticated and existing users.
     *
     * @param authentication The current authentication context.
     * @return The {@link User} entity associated with the authenticated user.
     * @throws AuthenticationException if the user cannot be found based on the authentication context.
     */
    private User getUserEntityOrThrow(Authentication authentication) {
        return userRepository.findUserByUsernameIgnoreCase(authentication.getName()).orElseThrow(
                AuthenticationException::new
        );
    }
}