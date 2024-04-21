package edu.mlm.ecommercestore.dto.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Data Transfer Object for returning rating details in responses.
 * This DTO includes the rating's unique identifier, the rating value itself, associated product and user IDs, and timestamps for creation and last update.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for returning rating details. Includes rating ID, value, associated product and user IDs, and creation/update timestamps.")
public class RatingResponseDTO {

    /**
     * The unique identifier of the rating.
     */
    @Schema(description = "The unique identifier of the rating.", example = "1")
    private long id;

    /**
     * The rating value, constrained between 1 (lowest) and 5 (highest).
     */
    @Schema(description = "The rating value, constrained between 1 and 5.", example = "4")
    private int rating;

    /**
     * The unique identifier of the product that was rated.
     */
    @Schema(description = "The unique identifier of the product that was rated.", example = "100")
    private long productId;

    /**
     * The unique identifier of the user who submitted the rating.
     */
    @Schema(description = "The unique identifier of the user who submitted the rating.", example = "50")
    private long userId;

    /**
     * The timestamp when the rating was created.
     */
    @Schema(description = "The timestamp when the rating was created.", example = "2023-01-01T12:00:00")
    private LocalDateTime createdAt;

    /**
     * The timestamp when the rating was last updated.
     */
    @Schema(description = "The timestamp when the rating was last updated.", example = "2023-01-02T12:00:00")
    private LocalDateTime updatedAt;
}
