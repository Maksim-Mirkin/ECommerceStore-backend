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
import edu.mlm.ecommercestore.service.filter.ProductSpecification;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.mapping.PropertyReferenceException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.Comparator;
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

        return getProductResponseDTO(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductListDTO findProducts(
            String name,
            List<String> brands,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            List<String> colors,
            List<String> memories,
            List<String> screenSizes,
            List<String> batteryCapacities,
            List<String> operatingSystems,
            List<String> categoryNames,
            int pageNumber,
            int pageSize,
            String sortDir,
            String sortBy
    ) {
        try {
            Specification<Product> spec = Specification.where(ProductSpecification.hasName(name))
                    .and(ProductSpecification.hasBrands(brands))
                    .and(ProductSpecification.hasPriceBetween(minPrice, maxPrice))
                    .and(ProductSpecification.hasColors(colors))
                    .and(ProductSpecification.hasMemories(memories))
                    .and(ProductSpecification.hasScreenSizes(screenSizes))
                    .and(ProductSpecification.hasBatteryCapacities(batteryCapacities))
                    .and(ProductSpecification.hasOperatingSystems(operatingSystems))
                    .and(ProductSpecification.byCategories(categoryNames));

            var pageRequest = PageRequest.of(pageNumber, pageSize);
            Page<Product> productPage;

            if ("ratings".equalsIgnoreCase(sortBy)) {
                val filteredProducts = productRepository.findAll(spec);

                filteredProducts.forEach(Product::calculateAverageRating);

                filteredProducts.sort(Comparator.comparingDouble(Product::getAverageRating));
                if ("desc".equalsIgnoreCase(sortDir)) {
                    Collections.reverse(filteredProducts);
                }

                val start = Math.min((int) pageRequest.getOffset(), filteredProducts.size());
                val end = Math.min((start + pageRequest.getPageSize()), filteredProducts.size());
                val pagedProducts = filteredProducts.subList(start, end);

                productPage = new PageImpl<>(pagedProducts, pageRequest, filteredProducts.size());
            } else {
                val direction = Sort.Direction.fromString(sortDir);
                pageRequest = PageRequest.of(pageNumber, pageSize, Sort.by(direction, sortBy));
                productPage = productRepository.findAll(spec, pageRequest);
            }

            if (pageNumber >= productPage.getTotalPages()) {
                throw new PaginationException("Page Number " + pageNumber + " Exceeds totalPages " + productPage.getTotalPages());
            }

            return mapProductPageToDTO(productPage);
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
        return getProductResponseDTO(product);
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
        return getProductResponseDTO(saved);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ProductResponseDTO deleteProduct(long id, Authentication authentication) {
        val product = getProductEntityOrThrow(id);
        checkPermission(authentication);
        productRepository.delete(product);
        return getProductResponseDTO(product);
    }


    /**
     * Converts a {@link Product} object to a {@link ProductResponseDTO}.
     * If the product has no average rating, a random rating between 1 and 5 is generated.
     *
     * @param product the product to convert
     * @return a ProductResponseDTO that represents the given product
     */
    private ProductResponseDTO getProductResponseDTO(Product product) {
        product.calculateAverageRating();
        if(product.getAverageRating() == 0){
            product.setAverageRating(1 + (Math.random() * 4));
        }
        val productResponseDTO = modelMapper.map(product, ProductResponseDTO.class);
        productResponseDTO.setCategory(product.getCategory().getName());
        return productResponseDTO;
    }

    /**
     * Maps a {@link Page} of {@link Product} objects to a {@link ProductListDTO}.
     * <p>
     * The method transforms a page of products into a DTO object containing product details and pagination information.
     * For each product, the average rating is calculated, and the product is mapped to a {@link ProductResponseDTO}.
     *
     * @param pr The page of products to be mapped.
     * @return A {@link ProductListDTO} containing product details and pagination information.
     */
    private ProductListDTO mapProductPageToDTO(Page<Product> pr) {
        val productListDTO =
                pr.getContent().stream()
                        .map(this::getProductResponseDTO)
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