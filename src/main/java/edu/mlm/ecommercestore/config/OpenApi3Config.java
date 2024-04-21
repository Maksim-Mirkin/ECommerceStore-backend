package edu.mlm.ecommercestore.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

/**
 * Configuration class for OpenAPI 3.0 specification details for the E-Commerce Store application.
 * <p>
 * This class uses annotations to define the OpenAPI information, including the application title, version,
 * description, contact information, and license. It also specifies the security scheme used by the application,
 * highlighting the authentication method.
 * <p>
 * The {@link OpenAPIDefinition} annotation outlines the general API information and the security requirements
 * for accessing the API endpoints, indicating that bearer authentication is required.
 * <p>
 * The {@link SecurityScheme} annotation details the security scheme named "Bearer Authentication", specifying
 * it as an HTTP security scheme using JWT tokens. This setup ensures that the API's documentation accurately
 * reflects its authentication mechanisms, providing essential information to developers and users interacting
 * with the API.
 * <p>
 * Through these annotations, the API documentation generated will include comprehensive and accurate details
 * about the e-commerce application, facilitating better understanding and usability of the API.
 */
@OpenAPIDefinition(
        info = @Info(
                title = "E-Commerce Store",
                version = "1.0",
                description = "E-Commerce Store Application",
                contact = @Contact(
                        name = "Maksim Mirkin",
                        url = "https://github.com/Maksim-Mirkin",
                        email = "maksim.mirkin@gmail.com"
                ),
                license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.txt")
        ),
        security = {
                @SecurityRequirement(
                        name = "Bearer Authentication"
                )
        }
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
public class OpenApi3Config {}
