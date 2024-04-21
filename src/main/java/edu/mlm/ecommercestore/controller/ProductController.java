package edu.mlm.ecommercestore.controller;

import edu.mlm.ecommercestore.dto.exception.ExceptionDTO;
import edu.mlm.ecommercestore.dto.exception.InternalServerExceptionDTO;
import edu.mlm.ecommercestore.dto.product.ProductListDTO;
import edu.mlm.ecommercestore.dto.product.ProductRequestDTO;
import edu.mlm.ecommercestore.dto.product.ProductResponseDTO;
import edu.mlm.ecommercestore.service.product.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Controller for managing products within the application.
 * Provides endpoints for creating, retrieving, updating, and deleting products.
 * Each method is documented with the expected HTTP response codes and conditions.
 * Note: Some operations require admin privileges.
 *
 * @author Maksim Mirkin
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Product", description = "Product API")
public class ProductController {

    private final ProductService productService;

    /**
     * Retrieves a paginated list of all products.
     * Supports pagination through query parameters.
     *
     * @param pageNumber the page number to retrieve, starting from 0.
     * @param pageSize the number of items per page.
     * @param sortDir the direction of sorting (asc or desc).
     * @param sortBy the property to sort the items by.
     * @return a {@link ProductListDTO} containing a list of {@link ProductResponseDTO}.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of products"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid page number/page size/sort direction/sort by property",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "You are not authorized to view the resource",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "The products you were trying to reach is not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerExceptionDTO.class)
                    )
            )
    })
    @Operation(summary = "Get all products",
            description = "Retrieves a list of all products with pagination support.")
    @GetMapping
    public ResponseEntity<ProductListDTO> getAllProducts(
            @RequestParam(value = "pageNumber", required = false, defaultValue = "0") int pageNumber,
            @RequestParam(value = "pageSize", required = false, defaultValue = "12") int pageSize,
            @RequestParam(value = "sortDir", required = false, defaultValue = "asc") String sortDir,
            @RequestParam(value = "sortBy", required = false, defaultValue = "id") String sortBy
    ) {
        return ResponseEntity.ok(productService.getAllProducts(pageNumber, pageSize, sortDir, sortBy));
    }


    /**
     * Retrieves detailed information about a product identified by its unique ID.
     *
     * @param id the unique identifier of the product to retrieve.
     * @return a {@link ProductResponseDTO} containing the product details.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved product"),
            @ApiResponse(responseCode = "401",
                    description = "You are not authorized to view the resource",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Invalid product id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerExceptionDTO.class)
                    )
            )
    })
    @Operation(summary = "Get a specific product by ID",
            description = "Retrieves detailed information about a product identified by its unique ID.")
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDTO> getProductById(@PathVariable long id) {
        return ResponseEntity.ok(productService.getProductById(id));
    }


    /**
     * Creates a new product in the system. Requires admin privileges.
     *
     * @param productDto the product data transfer object containing product details.
     * @param uriBuilder a {@link UriComponentsBuilder} for building the location URI.
     * @param authentication the {@link Authentication} object representing the current user's authentication.
     * @return a {@link ProductResponseDTO} with the created product's details.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Successfully created product"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid product request body/invalid product id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "You are not authorized to make this request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "You have no permission to create a product",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerExceptionDTO.class)
                    )
            )

    })
    @Operation(summary = "Create a new product",
            description = "Creates a new product in the system. Requires admin privileges.")
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> createProducts(
            @RequestBody @Valid ProductRequestDTO productDto,
            UriComponentsBuilder uriBuilder,
            Authentication authentication
    ) {
        var res = productService.createProduct(productDto, authentication);
        var uri = uriBuilder.path("/api/v1/products/{id}").buildAndExpand(res.getId()).toUri();
        return ResponseEntity.created(uri).body(res);
    }

    /**
     * Updates an existing product identified by its ID. Requires admin privileges.
     *
     * @param id the unique identifier of the product to update.
     * @param dto the product data transfer object containing updated product details.
     * @param authentication the {@link Authentication} object representing the current user's authentication.
     * @return a {@link ProductResponseDTO} with the updated product's details.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated product"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid product id or request body",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "You are not authorized to make this request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "You have no permission to update a product",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "The product you were trying to update is not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerExceptionDTO.class)
                    )
            )
    })
    @PutMapping("/{id}")
    @Operation(summary = "Update a product",
            description = "Updates an existing product identified by its ID." +
                    " Requires admin privileges.")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> updateProduct(
            @PathVariable long id,
            @RequestBody @Valid ProductRequestDTO dto,
            Authentication authentication
    ) {
        return ResponseEntity.ok(productService.updateProduct(id, dto, authentication));
    }

    /**
     * Deletes a product from the system by its ID. Requires admin privileges.
     *
     * @param id the unique identifier of the product to delete.
     * @param authentication the {@link Authentication} object representing the current user's authentication.
     * @return a {@link ProductResponseDTO} indicating the outcome of the delete operation.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully deleted product"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid product id",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "You are not authorized to make this request",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "403",
                    description = "You have no permission to delete a product",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "The product you were trying to delete is not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "500",
                    description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerExceptionDTO.class)
                    )
            )
    })
    @Operation(summary = "Delete a product",
            description = "Deletes a product from the system by its ID. Requires admin privileges.")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductResponseDTO> deleteProduct(
            @PathVariable long id,
            Authentication authentication
    ) {
        return ResponseEntity.ok(productService.deleteProduct(id, authentication));
    }
}
