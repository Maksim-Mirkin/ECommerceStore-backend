package edu.mlm.ecommercestore.controller;

import edu.mlm.ecommercestore.dto.exception.ExceptionDTO;
import edu.mlm.ecommercestore.dto.exception.InternalServerExceptionDTO;
import edu.mlm.ecommercestore.dto.rating.RatingRequestDTO;
import edu.mlm.ecommercestore.dto.rating.RatingResponseDTO;
import edu.mlm.ecommercestore.service.product.RatingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

/**
 * Controller for managing ratings related to products.
 * It allows for the submission of ratings for specific products by authenticated users.
 *
 * @author Maksim Mirkin
 * @version 1.0
 * @since 1.0
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
@Tag(name = "Rating", description = "Rating API")

public class RatingController {

    private final RatingService ratingService;

    /**
     * Submits a rating for a specific product identified by its ID.
     * Users must be authenticated to submit ratings.
     * Validates the submitted rating and associates it with the given product.
     *
     * @param productId      The ID of the product being rated.
     * @param dto            The rating request DTO containing the rating details.
     * @param authentication The authentication token of the user submitting the rating.
     * @param uriBuilder     A UriComponentsBuilder to help construct the location URI of the created rating.
     * @return {@link RatingResponseDTO} containing the rating response DTO along with the HTTP status.
     */
    @PostMapping("/{productId}/ratings")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Rating created successfully"),
            @ApiResponse(responseCode = "400",
                    description = "Invalid rating value",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "401",
                    description = "You are not authorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )
            ),
            @ApiResponse(responseCode = "404",
                    description = "Product not found",
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
    @Operation(summary = "Submit/update a product rating",
            description = "Submits/updates a rating for a specific product by an authenticated user." +
                    " The rating is validated and associated with the product.")
    public ResponseEntity<RatingResponseDTO> postRating(
            @PathVariable long productId,
            @RequestBody @Valid RatingRequestDTO dto,
            Authentication authentication,
            UriComponentsBuilder uriBuilder
    ) {
        var saved = ratingService.postRating(productId, dto, authentication);
        var uri = uriBuilder.path("/{productId}")
                .buildAndExpand(saved.getId())
                .toUri();
        return ResponseEntity.created(uri).body(saved);
    }

    @Operation(summary = "Update a product rating",
            description = "Updates an existing rating for a specific product by an authenticated user." +
                    " The rating is validated and updated for the product.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating updated successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RatingResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "400", description = "Invalid rating value",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )),
            @ApiResponse(responseCode = "401", description = "You are not authorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Product not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerExceptionDTO.class)
                    ))
    })
    @PutMapping("/{productId}/ratings")
    public ResponseEntity<RatingResponseDTO> updateRating(
            @PathVariable long productId,
            @RequestBody @Valid RatingRequestDTO dto,
            Authentication authentication
    ) {
        return ResponseEntity.ok(ratingService.updateRating(productId, dto, authentication));
    }

    /**
     * Retrieves the rating for a specific product given by an authenticated user.
     *
     * @param productId      the ID of the product whose rating is to be retrieved.
     * @param authentication the authentication object containing the current user's authentication details.
     * @return a {@link ResponseEntity} containing the {@link RatingResponseDTO}.
     */
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Rating retrieved successfully",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = RatingResponseDTO.class)
                    )),
            @ApiResponse(responseCode = "401", description = "You are not authorized",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )),
            @ApiResponse(responseCode = "404", description = "Rating not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ExceptionDTO.class)
                    )),
            @ApiResponse(responseCode = "500", description = "Internal server error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = InternalServerExceptionDTO.class)
                    ))
    })
    @Operation(summary = "Get a product rating",
            description = "Retrieves the rating for a specific product given by an authenticated user.")
    @GetMapping("/{productId}/ratings")
    public ResponseEntity<RatingResponseDTO> getRatingByProductId(
            @PathVariable long productId,
            Authentication authentication
    ) {
        return ResponseEntity.ok(ratingService.getRatingByProductId(productId, authentication));
    }
}