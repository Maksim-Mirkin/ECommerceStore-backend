package edu.mlm.ecommercestore.service.filter;

import edu.mlm.ecommercestore.dto.filter.ProductFilterOptionsDTO;
import edu.mlm.ecommercestore.entity.Category;
import edu.mlm.ecommercestore.entity.Product;
import edu.mlm.ecommercestore.repository.ProductRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * Service class to handle the retrieval of product filter options based on various criteria.
 */
@Service
@RequiredArgsConstructor
public class FilterService {
    private final ProductRepository productRepository;
    @PersistenceContext
    private EntityManager entityManager;

    /**
     * Retrieves filter options for products based on specified criteria including name, brands, price range, colors, memories, screen sizes, battery capacities, operating systems, and categories.
     * The method returns distinct lists of each attribute that are available in the database that match the given filtering criteria.
     *
     * @param name the product name to filter by.
     * @param brands list of brands to include in the filters.
     * @param minPrice minimum price to include in the filters.
     * @param maxPrice maximum price to include in the filters.
     * @param colors list of colors to include in the filters.
     * @param memories list of memory specifications to include in the filters.
     * @param screenSizes list of screen sizes to include in the filters.
     * @param batteryCapacities list of battery capacities to include in the filters.
     * @param operatingSystems list of operating systems to include in the filters.
     * @param categories list of categories to include in the filters.
     * @return a {@link ProductFilterOptionsDTO} object containing lists of distinct filter options.
     */
    public ProductFilterOptionsDTO getProductFilterOptions(
            String name,
            List<String> brands,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            List<String> colors,
            List<String> memories,
            List<String> screenSizes,
            List<String> batteryCapacities,
            List<String> operatingSystems,
            List<String> categories
    ) {

        val spec = Specification.where(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasBrands(brands))
                .and(ProductSpecification.hasPriceBetween(minPrice, maxPrice, entityManager))
                .and(ProductSpecification.hasColors(colors))
                .and(ProductSpecification.hasMemories(memories))
                .and(ProductSpecification.hasScreenSizes(screenSizes))
                .and(ProductSpecification.hasBatteryCapacities(batteryCapacities))
                .and(ProductSpecification.hasOperatingSystems(operatingSystems))
                .and(ProductSpecification.byCategories(categories));

        val products = productRepository.findAll(spec);

        val uniqueBrands = products.stream().map(Product::getBrand).distinct().toList();
        val uniquePrices = products.stream().map(Product::getPrice).distinct().toList();
        val uniqueColors = products.stream().map(Product::getColor).distinct().toList();
        val uniqueMemories = products.stream().map(Product::getMemory).distinct().toList();
        val uniqueScreenSizes = products.stream().map(Product::getScreenSize).distinct().toList();
        val uniqueBatteryCapacities = products.stream().map(Product::getBatteryCapacity).distinct().toList();
        val uniqueOperatingSystems = products.stream().map(Product::getOperatingSystem).distinct().toList();
        val uniqueCategories = products.stream().map(Product::getCategory).map(Category::getName).distinct().toList();

        return new ProductFilterOptionsDTO(
                uniqueBrands,
                uniquePrices,
                uniqueColors,
                uniqueMemories,
                uniqueScreenSizes,
                uniqueBatteryCapacities,
                uniqueOperatingSystems,
                uniqueCategories
        );
    }
}
