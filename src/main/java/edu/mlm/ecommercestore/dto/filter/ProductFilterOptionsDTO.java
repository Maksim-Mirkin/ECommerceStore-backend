package edu.mlm.ecommercestore.dto.filter;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

/**
 * Data Transfer Object containing lists of product attributes for filter options in the e-commerce store.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFilterOptionsDTO {

    @Schema(description = "List of available brands for filtering products")
    private List<String> brands;

    @Schema(description = "List of distinct prices available for filtering products")
    private List<BigDecimal> prices;

    @Schema(description = "List of available colors for filtering products")
    private List<String> colors;

    @Schema(description = "List of available memory capacities for filtering products")
    private List<String> memories;

    @Schema(description = "List of weights available for filtering products, typically measured in units like kg or lbs")
    private List<BigDecimal> weights;

    @Schema(description = "List of battery capacities available for filtering products, typically measured in mAh")
    private List<String> batteryCapacities;

    @Schema(description = "List of operating systems available for filtering products")
    private List<String> operatingSystems;

    @Schema(description = "List of product categories available for filtering")
    private List<String> categories;
}
