package edu.mlm.ecommercestore.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

/**
 * Represents a category of products in the e-commerce store.
 * Categories are used to organize products into logical groups, making it easier for users
 * to find products related to a specific category such as Cellular, TV, Headphones, Laptops.
 * <p>
 * This entity is mapped to the "categories" table in the database, with each category having
 * a unique identifier and a name.
 *
 * @author Maksim Mirkin
 * @version 1.0
 * @since 1.0
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "categories")
@Schema(description = "Represents a category of products in the e-commerce store")
public class Category {

    /**
     * The unique identifier of the category. This field is auto-generated in the database.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Schema(description = "The unique identifier of the category")
    private Long id;

    /**
     * The name of the category. It describes the category and is used to categorize products.
     * Include "Cellular", "TV", "Headphones", "Laptop". This field cannot be null.
     */
    @NotNull
    @Schema(description = "The name of the category", example = "Cellular/TV/Headphone/Laptop")
    private String name;
}
