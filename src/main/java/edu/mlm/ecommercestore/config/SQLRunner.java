package edu.mlm.ecommercestore.config;

import edu.mlm.ecommercestore.entity.Category;
import edu.mlm.ecommercestore.entity.Role;
import edu.mlm.ecommercestore.entity.User;
import edu.mlm.ecommercestore.repository.CategoryRepository;
import edu.mlm.ecommercestore.repository.RoleRepository;
import edu.mlm.ecommercestore.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Set;

/**
 * Initializes the database with default roles, users, and categories upon application startup.
 * <p>
 * This configuration class implements Spring Boot's {@link CommandLineRunner} to execute initial
 * data loading operations. It checks if the roles and categories are already populated in the database
 * and, if not, inserts default values. It also creates default admin and customer users with encoded
 * passwords for initial access to the system.
 * <p>
 * This setup ensures that the application is ready to be used right after startup, with essential
 * data structures and entries in place.
 */
@Configuration
@RequiredArgsConstructor
@Transactional
public class SQLRunner implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final CategoryRepository categoryRepository;

    /**
     * Executes the initial data loading logic.
     * <p>
     * Checks the presence of roles and categories in their respective repositories and populates
     * them if they are found to be empty. It also ensures the creation of default admin and customer
     * user accounts.
     *
     * @param args command line arguments passed during application startup, not used in this implementation
     * @throws Exception if an error occurs during data initialization
     */
    @Transactional
    @Override
    public void run(String... args) throws Exception {
        // Roles initialization
        if (roleRepository.count() == 0) {
            val adminRole = roleRepository.save(new Role(1L, "ROLE_ADMIN"));
            val customerRole = roleRepository.save(new Role(2L, "ROLE_CUSTOMER"));
            // Default users creation
            userRepository.save(
                    new User(
                            1L,
                            "admin",
                            "admin@admin.com",
                            passwordEncoder.encode("Pass0rd1!"),
                            Set.of(adminRole)
                    )
            );
            userRepository.save(
                    new User(
                            2L,
                            "customer",
                            "customer@customer.com",
                            passwordEncoder.encode("Pass0rd1!"),
                            Set.of(customerRole)
                    )
            );
        }

        // Categories initialization
        if (categoryRepository.count() == 0) {
            categoryRepository.save(new Category(1L, "Laptop"));
            categoryRepository.save(new Category(2L, "Cellular"));
            categoryRepository.save(new Category(3L, "TV"));
            categoryRepository.save(new Category(4L, "Headphone"));
        }
    }
}

