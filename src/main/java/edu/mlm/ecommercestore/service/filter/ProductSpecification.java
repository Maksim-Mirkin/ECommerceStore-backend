package edu.mlm.ecommercestore.service.filter;

import edu.mlm.ecommercestore.entity.Category;
import edu.mlm.ecommercestore.entity.Product;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;
import java.util.List;

/**
 * Utility class providing static methods to create JPA Specifications for filtering Product entities based on various criteria.
 */
public class ProductSpecification {

    /**
     * Specification to filter products by name.
     *
     * @param name the name to search for.
     * @return a specification that matches products whose names contain the given substring, case-insensitively.
     */
    public static Specification<Product> hasName(String name) {
        return (root, query, criteriaBuilder) -> {
            if (name == null) {
                return criteriaBuilder.isTrue(criteriaBuilder.literal(true));
            }
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
        };
    }

    /**
     * Specification to filter products by a list of brands.
     *
     * @param brands a list of brands to filter by.
     * @return a specification that matches products belonging to any of the specified brands.
     */
    public static Specification<Product> hasBrands(List<String> brands) {
        return (root, query, criteriaBuilder) -> {
            if (brands == null || brands.isEmpty()) {
                return null;
            } else {
                CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("brand"));
                brands.forEach(inClause::value);
                return inClause;
            }
        };
    }

    /**
     * Specification to filter products by price range.
     *
     * @param minPrice minimum price filter value.
     * @param maxPrice maximum price filter value.
     * @return a specification that matches products whose prices fall between the specified range.
     */
    public static Specification<Product> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice) {
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (minPrice == null && maxPrice == null) {
                return null;
            } else if (minPrice == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), maxPrice);
            } else if (maxPrice == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), minPrice);
            } else {
                return criteriaBuilder.between(root.get("price"), minPrice, maxPrice);
            }
        };
    }

    /**
     * Specification to filter products by a list of colors.
     *
     * @param colors a list of colors to filter by.
     * @return a specification that matches products with any of the specified colors.
     */
    public static Specification<Product> hasColors(List<String> colors) {
        return (root, query, criteriaBuilder) -> {
            if (colors == null || colors.isEmpty()) {
                return null;
            } else {
                CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("color"));
                colors.forEach(color -> inClause.value(color.trim()));
                return inClause;
            }
        };
    }

    /**
     * Specification to filter products by a list of memory capacities.
     *
     * @param memories a list of memory capacities to filter by.
     * @return a specification that matches products with any of the specified memory capacities.
     */
    public static Specification<Product> hasMemories(List<String> memories) {
        return (root, query, criteriaBuilder) -> {
            if (memories == null || memories.isEmpty()) {
                return null;
            } else {
                CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("memory"));
                memories.forEach(memory -> inClause.value(memory.trim()));
                return inClause;
            }
        };
    }

    /**
     * Specification to filter products by a list of screen sizes.
     *
     * @param screenSizes a list of screen sizes to filter by.
     * @return a specification that matches products with any of the specified screen sizes.
     */
    public static Specification<Product> hasScreenSizes(List<String> screenSizes) {
        return (root, query, criteriaBuilder) -> {
            if (screenSizes == null || screenSizes.isEmpty()) {
                return null;
            } else {
                CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("screenSize"));
                screenSizes.forEach(screenSize -> inClause.value(screenSize.trim()));
                return inClause;
            }
        };
    }

    /**
     * Specification to filter products by a list of battery capacities.
     *
     * @param batteryCapacities a list of battery capacities to filter by.
     * @return a specification that matches products with any of the specified battery capacities.
     */
    public static Specification<Product> hasBatteryCapacities(List<String> batteryCapacities) {
        return (root, query, criteriaBuilder) -> {
            if (batteryCapacities == null || batteryCapacities.isEmpty()) {
                return null;
            } else {
                CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("batteryCapacity"));
                batteryCapacities.forEach(batteryCapacity -> inClause.value(batteryCapacity.trim()));
                return inClause;
            }
        };
    }

    /**
     * Specification to filter products by a list of operating systems.
     *
     * @param operatingSystems a list of operating systems to filter by.
     * @return a specification that matches products with any of the specified operating systems.
     */
    public static Specification<Product> hasOperatingSystems(List<String> operatingSystems) {
        return (root, query, criteriaBuilder) -> {
            if (operatingSystems == null || operatingSystems.isEmpty()) {
                return null;
            } else {
                CriteriaBuilder.In<String> inClause = criteriaBuilder.in(root.get("operatingSystem"));
                operatingSystems.forEach(os -> inClause.value(os.trim()));
                return inClause;
            }
        };
    }

    /**
     * Specification to filter products by categories using category names.
     *
     * @param categoryNames a list of category names to filter by.
     * @return a specification that matches products belonging to any of the specified categories.
     */
    public static Specification<Product> byCategories(List<String> categoryNames) {
        return (root, query, cb) -> {
            if (categoryNames == null || categoryNames.isEmpty()) {
                return null;
            } else {
                Join<Product, Category> categoryJoin = root.join("category");
                CriteriaBuilder.In<String> inClause = cb.in(cb.lower(categoryJoin.get("name")));
                categoryNames.forEach(name -> inClause.value(name.trim().toLowerCase()));
                return inClause;
            }
        };
    }
}
