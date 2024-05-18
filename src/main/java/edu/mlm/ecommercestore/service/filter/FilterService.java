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
            List<String> colors,
            List<String> memories,
            List<String> screenSizes,
            List<String> batteryCapacities,
            List<String> operatingSystems,
            List<String> categories
    ) {
        val spec = Specification.where(ProductSpecification.hasName(name))
                .and(ProductSpecification.hasBrands(brands))
                .and(ProductSpecification.hasColors(colors))
                .and(ProductSpecification.hasMemories(memories))
                .and(ProductSpecification.hasScreenSizes(screenSizes))
                .and(ProductSpecification.hasBatteryCapacities(batteryCapacities))
                .and(ProductSpecification.hasOperatingSystems(operatingSystems))
                .and(ProductSpecification.byCategories(categories));

        BigDecimal minPriceForSpec = getMinPriceForSpec(spec);
        BigDecimal maxPriceForSpec = getMaxPriceForSpec(spec);

        val priceSpec = ProductSpecification.hasPriceBetween(minPriceForSpec, maxPriceForSpec);

        val finalSpec = spec.and(priceSpec);

        val products = productRepository.findAll(finalSpec);

        val uniqueBrands = products.stream().map(Product::getBrand).distinct().toList();
        val priceRange = List.of(minPriceForSpec, maxPriceForSpec);
        val uniqueColors = products.stream().map(Product::getColor).distinct().toList();
        val uniqueMemories = products.stream().map(Product::getMemory).distinct().toList();
        val uniqueScreenSizes = products.stream().map(Product::getScreenSize).distinct().toList();
        val uniqueBatteryCapacities = products.stream().map(Product::getBatteryCapacity).distinct().toList();
        val uniqueOperatingSystems = products.stream().map(Product::getOperatingSystem).distinct().toList();
        val uniqueCategories = products.stream().map(Product::getCategory).map(Category::getName).distinct().toList();

        return new ProductFilterOptionsDTO(
                uniqueBrands,
                priceRange,
                uniqueColors,
                uniqueMemories,
                uniqueScreenSizes,
                uniqueBatteryCapacities,
                uniqueOperatingSystems,
                uniqueCategories
        );
    }

    /**
     * Retrieves the minimum price of products that match the given specification.
     * <p>
     * This method constructs and executes a query to find the minimum price of all products
     * that satisfy the criteria defined in the provided specification.
     *
     * @param spec the specification defining the criteria for filtering products.
     * @return the minimum price of products that match the specification.
     */
    private BigDecimal getMinPriceForSpec(Specification<Product> spec) {
        val criteriaBuilder = entityManager.getCriteriaBuilder();
        val query = criteriaBuilder.createQuery(BigDecimal.class);
        val root = query.from(Product.class);
        query.select(criteriaBuilder.min(root.get("price"))).where(spec.toPredicate(root, query, criteriaBuilder));
        return entityManager.createQuery(query).getSingleResult();
    }

    /**
     * Retrieves the maximum price of products that match the given specification.
     * <p>
     * This method constructs and executes a query to find the maximum price of all products
     * that satisfy the criteria defined in the provided specification.
     *
     * @param spec the specification defining the criteria for filtering products.
     * @return the maximum price of products that match the specification.
     */
    private BigDecimal getMaxPriceForSpec(Specification<Product> spec) {
        val criteriaBuilder = entityManager.getCriteriaBuilder();
        val query = criteriaBuilder.createQuery(BigDecimal.class);
        val root = query.from(Product.class);
        query.select(criteriaBuilder.max(root.get("price"))).where(spec.toPredicate(root, query, criteriaBuilder));
        return entityManager.createQuery(query).getSingleResult();
    }
}
