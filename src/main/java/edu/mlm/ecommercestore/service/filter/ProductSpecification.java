package edu.mlm.ecommercestore.service.filter;

import edu.mlm.ecommercestore.entity.Category;
import edu.mlm.ecommercestore.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Join;
import lombok.val;
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
        return (root, query, criteriaBuilder) ->
                criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

    /**
     * Retrieves the minimum product price from the database.
     *
     * @param entityManager the EntityManager to facilitate the query.
     * @return the minimum price of all products as BigDecimal.
     */
    public static BigDecimal getMinPrice(EntityManager entityManager) {
        val criteriaBuilder = entityManager.getCriteriaBuilder();
        val query = criteriaBuilder.createQuery(BigDecimal.class);
        val root = query.from(Product.class);
        query.select(criteriaBuilder.min(root.get("price")));
        return entityManager.createQuery(query).getSingleResult();
    }

    /**
     * Retrieves the maximum product price from the database.
     *
     * @param entityManager the EntityManager to facilitate the query.
     * @return the maximum price of all products as BigDecimal.
     */
    public static BigDecimal getMaxPrice(EntityManager entityManager) {
        val criteriaBuilder = entityManager.getCriteriaBuilder();
        val query = criteriaBuilder.createQuery(BigDecimal.class);
        val root = query.from(Product.class);
        query.select(criteriaBuilder.max(root.get("price")));
        return entityManager.createQuery(query).getSingleResult();
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
     * @param minPrice      minimum price filter value.
     * @param maxPrice      maximum price filter value.
     * @param entityManager the EntityManager to handle dynamic fetching of minimum and maximum prices if needed.
     * @return a specification that matches products whose prices fall between the specified range.
     */
    public static Specification<Product> hasPriceBetween(BigDecimal minPrice, BigDecimal maxPrice, EntityManager entityManager) {
        val actualMinPrice = getMinPrice(entityManager);
        val actualMaxPrice = getMaxPrice(entityManager);

        minPrice = (minPrice == null || minPrice.compareTo(actualMinPrice) < 0) ? actualMinPrice : minPrice;
        maxPrice = (maxPrice == null || maxPrice.compareTo(actualMaxPrice) > 0) ? actualMaxPrice : maxPrice;

        val finalMinPrice = minPrice;
        val finalMaxPrice = maxPrice;
        return (root, criteriaQuery, criteriaBuilder) -> {
            if (finalMinPrice == null && finalMaxPrice == null) {
                return null;
            } else if (finalMinPrice == null) {
                return criteriaBuilder.lessThanOrEqualTo(root.get("price"), finalMaxPrice);
            } else if (finalMaxPrice == null) {
                return criteriaBuilder.greaterThanOrEqualTo(root.get("price"), finalMinPrice);
            } else {
                return criteriaBuilder.between(root.get("price"), finalMinPrice, finalMaxPrice);
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
     * Specification to filter products by a list of weights.
     *
     * @param weights a list of weights to filter by.
     * @return a specification that matches products with any of the specified weights.
     */
    public static Specification<Product> hasWeights(List<BigDecimal> weights) {
        return (root, query, criteriaBuilder) -> {
            if (weights == null || weights.isEmpty()) {
                return null;
            } else {
                CriteriaBuilder.In<BigDecimal> inClause = criteriaBuilder.in(root.get("weight"));
                weights.forEach(inClause::value);
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
