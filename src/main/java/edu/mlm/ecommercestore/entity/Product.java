package edu.mlm.ecommercestore.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "products", indexes = {
        @Index(name = "idx_product_brand", columnList = "brand"),
        @Index(name = "idx_product_price", columnList = "price"),
        @Index(name = "idx_product_color", columnList = "color"),
        @Index(name = "idx_product_memory", columnList = "memory"),
        @Index(name = "idx_product_screen_size", columnList = "screenSize"),
        @Index(name = "idx_product_battery_capacity", columnList = "batteryCapacity"),
        @Index(name = "idx_product_operatingSystem", columnList = "operatingSystem")
})
@Schema(description = "Represents a product in the e-commerce store")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    @Schema(description = "The unique identifier of the product")
    private Long id;

    @NotNull
    @Schema(description = "The name of the product", example = "iPhone 12")
    private String name;

    @NotNull
    @Schema(description = "The brand of the product", example = "Apple")
    private String brand;

    @NotNull
    @Schema(description = "The price of the product", example = "999.99")
    private BigDecimal price;

    @NotNull
    @Schema(description = "The URL to the product image", example = "http://example.com/product.jpg")
    private String image;

    @NotNull
    @Size(max = 512)
    @Schema(
            description = "A brief description of the product",
            example = "Latest model with improved features"
    )
    private String description;

    @Schema(description = "The memory capacity of the product", example = "128GB")
    private String memory;

    @Schema(description = "The screen size of the product in inches", example = "11\"")
    private String screenSize;

    @Schema(description = "The battery capacity of the product", example = "2815mAh")
    private String batteryCapacity;

    @Schema(description = "The operating system of the product", example = "iOS")
    private String operatingSystem;

    @NotNull
    @Schema(description = "The color of the product", example = "Black")
    private String color;

    @NotNull
    @JoinColumn(name = "category_id", referencedColumnName = "id")
    @ManyToOne
    @Schema(description = "The category this product belongs to")
    private Category category;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @Schema(description = "List of ratings for the product")
    private List<Rating> ratings;

    @Transient
    @Schema(description = "The average rating of the product", example = "4.5")
    private double averageRating;

    @CreationTimestamp
    @Schema(description = "The date and time when the product was added to the store")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Schema(description = "The date and time when the product details were last updated")
    private LocalDateTime updatedAt;

    public void calculateAverageRating() {
        if (ratings == null || ratings.isEmpty()) {
            averageRating = 0;
            return;
        }

        int totalRatings = ratings.size();
        int sumOfRatings = 0;
        for (Rating rating : ratings) {
            sumOfRatings += rating.getRating();
        }
        averageRating = (double) sumOfRatings / totalRatings;
    }
}