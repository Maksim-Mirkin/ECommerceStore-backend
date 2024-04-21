package edu.mlm.ecommercestore.dto.rating;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Data Transfer Object for submitting a rating.
 * Allows clients to specify a rating value, typically for a product or service, within the range of 1 to 5.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "Data Transfer Object for submitting a rating. Specifies a rating value within the range of 1 to 5.")
public class RatingRequestDTO {

    /**
     * The rating value to be submitted, constrained between 1 (lowest) and 5 (highest).
     */
    @Min(1)
    @Max(5)
    @NotNull
    @Schema(description = "The rating value, constrained between 1 (lowest) and 5 (highest).", example = "4")
    private int rating;
}
