package edu.mlm.ecommercestore.service.product;

import edu.mlm.ecommercestore.dto.product.ProductListDTO;
import edu.mlm.ecommercestore.dto.product.ProductRequestDTO;
import edu.mlm.ecommercestore.dto.product.ProductResponseDTO;
import edu.mlm.ecommercestore.entity.Product;
import org.springframework.security.core.Authentication;

/**
 * Service interface for managing product operations in the e-commerce platform.
 * <p>
 * Defines the contract for services handling the creation, retrieval, update, and deletion of products.
 * This interface abstracts the core product management functionalities, ensuring that the system can
 * manage products efficiently while maintaining a clear separation of concerns between the service
 * layer and the persistence layer.
 */
public interface ProductService {

    /**
     * Creates a new product in the system based on the provided data.
     * <p>
     * This method handles the processing of product creation requests, ensuring that the product data
     * is validated and persisted. It may also perform authorization checks to ensure that the caller
     * has the necessary permissions to create products.
     *
     * @param productDto The product data transfer object containing the product details.
     * @param authentication The authentication context of the user performing the operation.
     * @return A {@link ProductResponseDTO} containing the details of the newly created product.
     */
    ProductResponseDTO createProduct(ProductRequestDTO productDto, Authentication authentication);

    /**
     * Retrieves a paginated list of all products.
     * <p>
     * Supports pagination and sorting to efficiently manage and display a large number of products.
     *
     * @param pageNumber The page number of the requested page.
     * @param pageSize The number of products per page.
     * @param sortDir The direction of the sort (e.g., "asc" or "desc").
     * @param sortBy The properties to sort the products by.
     * @return A {@link ProductListDTO} containing the paginated list of products.
     */
    ProductListDTO getAllProducts(int pageNumber, int pageSize, String sortDir, String... sortBy);

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
     * @param id The ID of the product to update.
     * @param dto The product data transfer object containing the updated product details.
     * @param authentication The authentication context of the user performing the operation.
     * @return A {@link ProductResponseDTO} containing the updated details of the product.
     */
    ProductResponseDTO updateProduct(long id, ProductRequestDTO dto, Authentication authentication);

    /**
     * Deletes a product from the system.
     * <p>
     * This method removes the product identified by the given ID from the database. It may perform
     * authorization checks to ensure that the caller has the necessary permissions to delete the product.
     *
     * @param id The ID of the product to delete.
     * @param authentication The authentication context of the user performing the operation.
     * @return A {@link ProductResponseDTO} containing the details of the deleted product.
     */
    ProductResponseDTO deleteProduct(long id, Authentication authentication);
}
