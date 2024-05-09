package edu.mlm.ecommercestore.controller;

import edu.mlm.ecommercestore.dto.filter.ProductFilterOptionsDTO;
import edu.mlm.ecommercestore.service.filter.FilterService;
import edu.mlm.ecommercestore.validator.AllowedParameters;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.List;

/**
 * REST controller for handling requests to retrieve filter options for products based on various attributes.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/filter")
public class FilterController {

    private final FilterService filterService;

    /**
     * Endpoint to retrieve product filter options based on specified criteria including product name and other attributes.
     *
     * @param name              the product name to filter by.
     * @param brands            list of brands to filter by.
     * @param minPrice          minimum price to filter by.
     * @param maxPrice          maximum price to filter by.
     * @param colors            list of colors to filter by.
     * @param memories          list of memory specifications to filter by.
     * @param weights           list of weights to filter by.
     * @param batteryCapacities list of battery capacities to filter by.
     * @param operatingSystems  list of operating systems to filter by.
     * @param categories        list of categories to filter by.
     * @return a {@link ProductFilterOptionsDTO} with the distinct filter options available based on the criteria.
     */
    @AllowedParameters({"name", "brand", "minPrice", "maxPrice", "color", "memory", "weight", "batteryCapacity", "operatingSystem", "category"})
    @GetMapping
    public ResponseEntity<ProductFilterOptionsDTO> getFilterOptions(
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "brand", required = false) List<String> brands,
            @RequestParam(value = "minPrice", required = false) BigDecimal minPrice,
            @RequestParam(value = "maxPrice", required = false) BigDecimal maxPrice,
            @RequestParam(value = "color", required = false) List<String> colors,
            @RequestParam(value = "memory", required = false) List<String> memories,
            @RequestParam(value = "weight", required = false) List<BigDecimal> weights,
            @RequestParam(value = "batteryCapacity", required = false) List<String> batteryCapacities,
            @RequestParam(value = "operatingSystem", required = false) List<String> operatingSystems,
            @RequestParam(value = "category", required = false) List<String> categories) {

        ProductFilterOptionsDTO options = filterService.getProductFilterOptions(
                name,
                brands,
                minPrice,
                maxPrice,
                colors,
                memories,
                weights,
                batteryCapacities,
                operatingSystems,
                categories
        );
        return ResponseEntity.ok(options);
    }
}
