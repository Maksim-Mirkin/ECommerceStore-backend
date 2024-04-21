package edu.mlm.ecommercestore.service.product;

import edu.mlm.ecommercestore.dto.product.*;
import edu.mlm.ecommercestore.entity.Category;
import edu.mlm.ecommercestore.entity.Product;
import edu.mlm.ecommercestore.entity.User;
import edu.mlm.ecommercestore.error.AuthenticationException;
import edu.mlm.ecommercestore.error.InvalidPropertyException;
import edu.mlm.ecommercestore.error.PaginationException;
import edu.mlm.ecommercestore.error.ResourceNotFoundException;
import edu.mlm.ecommercestore.repository.CategoryRepository;
import edu.mlm.ecommercestore.repository.ProductRepository;
import edu.mlm.ecommercestore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service implementation for managing product operations.
 * <p>
 * This class implements the {@link ProductService} interface, providing concrete methods for
 * creating, retrieving, updating, and deleting products. It utilizes {@link ProductRepository},
 * {@link UserRepository}, and {@link CategoryRepository} for persistence operations, and
 * {@link ModelMapper} for object mapping between entities and DTOs.
 */
@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productDto, Authentication authentication) {
        var product = modelMapper.map(productDto, Product.class);
        val category = getCategoryEntityOrThrow(productDto.getCategory());
        product.setCategory(category);
        val saved = productRepository.save(product);

        return modelMapper.map(saved, ProductResponseDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductListDTO getAllProducts(
            int pageNumber,
            int pageSize,
            String sortDir,
            String... sortBy
    ) {
        try {
            Sort.Direction sort = Sort.Direction.fromString(sortDir);

            Pageable pageable = PageRequest.of(pageNumber, pageSize, sort, sortBy);

            Page<Product> pr = productRepository.findAll(pageable);

            if (pageNumber >= pr.getTotalPages()) {
                throw new PaginationException("Page number " + pageNumber + " exceeds total pages " + pr.getTotalPages());
            }

            List<ProductResponseDTO> productListDTO =
                    pr.getContent().stream()
                            .map(p -> {
                                p.calculateAverageRating();
                                return modelMapper.map(p, ProductResponseDTO.class);
                            })
                            .toList();
            return new ProductListDTO(
                    pr.getTotalElements(),
                    pr.getNumber(),
                    pr.getSize(),
                    pr.getTotalPages(),
                    pr.isFirst(),
                    pr.isLast(),
                    productListDTO
            );
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
    public ProductResponseDTO getProductById(long id) {
        Product product = getProductEntityOrThrow(id);
        product.calculateAverageRating();
        return modelMapper.map(product, ProductResponseDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Product getProductEntityOrThrow(long id) {
        return productRepository.findById(id).orElseThrow(ResourceNotFoundException.newInstance("Product", "id", id));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductResponseDTO updateProduct(long id, ProductRequestDTO dto, Authentication authentication) {
        val product = getProductEntityOrThrow(id);
        checkPermission(authentication);
        val category = getCategoryEntityOrThrow(dto.getCategory());
        var productBeforeSave = modelMapper.map(dto, Product.class);
        productBeforeSave.setId(id);
        productBeforeSave.setCategory(category);
        productBeforeSave.setRatings(product.getRatings());
        productBeforeSave.setCreatedAt(product.getCreatedAt());

        val saved = productRepository.save(productBeforeSave);
        saved.calculateAverageRating();
        return modelMapper.map(saved, ProductResponseDTO.class);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductResponseDTO deleteProduct(long id, Authentication authentication) {
        val product = getProductEntityOrThrow(id);
        checkPermission(authentication);
        productRepository.delete(product);
        product.calculateAverageRating();
        return modelMapper.map(product, ProductResponseDTO.class);
    }

    /**
     * Validates user permission for product management operations, ensuring the user has an admin role.
     *
     * @param authentication The authentication context of the current user.
     * @throws AuthenticationException if the user does not have the necessary permissions.
     */
    private void checkPermission(Authentication authentication) {
        val user = getUserEntityOrThrow(authentication);

        boolean isAdmin = user
                .getRoles().stream()
                .anyMatch(r -> r.getRoleName().equalsIgnoreCase("ROLE_ADMIN"));
        if (!isAdmin) {
            throw new AuthenticationException(user.getUsername() + " has no permission for this action");
        }
    }

    /**
     * Retrieves a {@link User} entity based on the authentication context or throws an exception if not found.
     *
     * @param authentication The authentication context of the current user.
     * @return The {@link User} entity.
     * @throws AuthenticationException if the user cannot be found.
     */
    private User getUserEntityOrThrow(Authentication authentication) {
        return userRepository.findUserByUsernameIgnoreCase(authentication.getName()).orElseThrow(
                () -> new AuthenticationException("User not found " + authentication.getName())
        );
    }

    /**
     * Retrieves a {@link Category} entity by its name or throws an exception if not found.
     *
     * @param category The name of the category to retrieve.
     * @return The {@link Category} entity.
     * @throws ResourceNotFoundException if the category cannot be found.
     */
    private Category getCategoryEntityOrThrow(String category) {
        return categoryRepository.findCategoryByNameIgnoreCase(category).orElseThrow(
                ResourceNotFoundException.newInstance("Category", "name", category)
        );
    }
}