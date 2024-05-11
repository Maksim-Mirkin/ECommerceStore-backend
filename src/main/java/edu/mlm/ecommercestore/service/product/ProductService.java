package edu.mlm.ecommercestore.service.product;

import edu.mlm.ecommercestore.dto.product.ProductListDTO;
import edu.mlm.ecommercestore.dto.product.ProductRequestDTO;
import edu.mlm.ecommercestore.dto.product.ProductResponseDTO;
import edu.mlm.ecommercestore.entity.Product;
import edu.mlm.ecommercestore.error.AuthenticationException;
import edu.mlm.ecommercestore.error.InvalidPropertyException;
import edu.mlm.ecommercestore.error.PaginationException;
import edu.mlm.ecommercestore.repository.CategoryRepository;
import edu.mlm.ecommercestore.repository.ProductRepository;
import edu.mlm.ecommercestore.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service implementation for managing product operations.
 * <p>
 * This class implements the {@link ProductService} interface, providing concrete methods for
 * creating, retrieving, updating, and deleting products. It utilizes {@link ProductRepository},
 * {@link UserRepository}, and {@link CategoryRepository} for persistence operations, and
 * {@link ModelMapper} for object mapping between entities and DTOs.
 * <p>
 * The supported operations include:
 * <ul>
 *     <li>Creating a new product</li>
 *     <li>Retrieving products (with pagination and optional filtering by category)</li>
 *     <li>Retrieving a product by its ID</li>
 *     <li>Updating an existing product</li>
 *     <li>Deleting a product</li>
 * </ul>
 */
public interface ProductService {

    /**
     * Creates a new product in the system based on the provided data.
     * <p>
     * This method handles the processing of product creation requests, ensuring that the product data
     * is validated and persisted. It may also perform authorization checks to ensure that the caller
     * has the necessary permissions to create products.
     *
     * @param productDto     The product data transfer object containing the product details.
     * @param authentication The authentication context of the user performing the operation.
     * @return A {@link ProductResponseDTO} containing the details of the newly created product.
     */
    ProductResponseDTO createProduct(ProductRequestDTO productDto, Authentication authentication);


    /**
     * Retrieves products based on specified criteria.
     * <p>
     * This method retrieves a list of products based on the provided criteria, such as name, brands,
     * price range, colors, etc. It supports pagination to limit the number of results returned.
     *
     * @param name              The name of the product (optional).
     * @param brands            The list of brands to filter by (optional).
     * @param minPrice          The minimum price of products to include (optional).
     * @param maxPrice          The maximum price of products to include (optional).
     * @param colors            The list of colors to filter by (optional).
     * @param memories          The list of memory capacities to filter by (optional).
     * @param screenSizes       The list of screen sizes to filter by (optional).
     * @param batteryCapacities The list of battery capacities to filter by (optional).
     * @param operatingSystems  The list of operating systems to filter by (optional).
     * @param categoryNames     The list of category names to filter by (optional).
     * @param pageNumber        The page number for pagination.
     * @param pageSize          The page size for pagination.
     * @param sortDir           The sort direction (ascending or descending).
     * @param sortBy            The field(s) to sort by.
     * @return A {@link ProductListDTO} containing the list of products matching the criteria.
     * @throws PaginationException      if pagination parameters are invalid or if the specified page number exceeds the total pages.
     * @throws InvalidPropertyException if a property referenced in sorting or filtering criteria is invalid.
     */
    ProductListDTO findProducts(
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
            String... sortBy
    );

    /**
     * Retrieves the details of a product by its ID.
     * <p>
     * If no product is found with the given ID, this method may throw a {@link edu.mlm.ecommercestore.error.ResourceNotFoundException}.
     *
     * @param id The ID of the product to retrieve.
     * @return A {@link ProductResponseDTO} containing the details of the requested product.
     */
    ProductResponseDTO getProductById(long id);

    /**
     * Retrieves a {@link Product} entity by its ID or throws an exception if not found.
     * <p>
     * This helper method is used internally to fetch product entities from the database, ensuring
     * that operations such as updates or deletions are performed on existing products.
     *
     * @param id The ID of the product to retrieve.
     * @return The {@link Product} entity.
     */
    Product getProductEntityOrThrow(long id);

    /**
     * Updates the details of an existing product.
     * <p>
     * This method handles product update requests, applying the provided changes to the product
     * identified by the given ID. Authorization checks should ensure that the caller has the
     * necessary permissions to update the product.
     *
     * @param id             The ID of the product to update.
     * @param dto            The product data transfer object containing the updated product details.
     * @param authentication The authentication context of the user performing the operation.
     * @return A {@link ProductResponseDTO} containing the updated details of the product.
     * @throws AuthenticationException if the user does not have the necessary permissions.
     */
    ProductResponseDTO updateProduct(long id, ProductRequestDTO dto, Authentication authentication);

    /**
     * Deletes a product from the system.
     * <p>
     * This method removes the product identified by the given ID from the database. It may perform
     * authorization checks to ensure that the caller has the necessary permissions to delete the product.
     *
     * @param id             The ID of the product to delete.
     * @param authentication The authentication context of the user performing the operation.
     * @return A {@link ProductResponseDTO} containing the details of the deleted product.
     */
    ProductResponseDTO deleteProduct(long id, Authentication authentication);
}
