package edu.mlm.ecommercestore.security;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import edu.mlm.ecommercestore.config.RSAKeyProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

/**
 * Security configuration class for the e-commerce application.
 * <p>
 * Configures web security, method security, and the OAuth2 resource server settings for handling JWT-based authentication.
 * It outlines the security filter chain, JWT decoder, and JWT encoder beans, essential for securing the application's
 * RESTful APIs. This class plays a pivotal role in defining access controls and ensuring that sensitive operations
 * are protected through stateless session management and JWT authentication.
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final RSAKeyProperties keyProperties;

    /**
     * Configures the CORS (Cross-Origin Resource Sharing) policy for the application.
     * <p>
     * Defines which origins, HTTP methods, and headers are allowed when making cross-origin requests
     * to the application. This setup is critical for web applications where the frontend and backend are
     * served from different origins and need to interact securely.
     * <p>
     * The configured policy:
     * <ul>
     *   <li>Allows specific origins (e.g., development server locations) to make requests.</li>
     *   <li>Enables credentials to be included in cross-origin requests.</li>
     *   <li>Specifies which HTTP headers can be used in requests.</li>
     *   <li>Defines the HTTP methods allowed from cross-origin requests.</li>
     * </ul>
     * Adjustments to the CORS policy should be made with consideration of security implications, ensuring
     * that the application remains protected against cross-origin attacks while allowing legitimate frontend
     * access.
     *
     * @return a {@link CorsConfigurationSource} that provides the CORS configuration for the application
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://127.0.0.1:5173", "http://localhost:5173"));
        configuration.setAllowCredentials(true);
        configuration.setAllowedHeaders(Arrays.asList("Access-Control-Allow-Headers", "Access-Control-Allow-Origin", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Origin", "Cache-Control", "Content-Type", "Authorization"));
        configuration.setAllowedMethods(Arrays.asList("DELETE", "GET", "POST", "PATCH", "PUT"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Defines the security filter chain for the application.
     * <p>
     * Configures CSRF protection, session management, and request authorization specifics.
     * It specifies which endpoints are publicly accessible and which require authentication.
     * Additionally, it configures OAuth2 resource server support to handle JWT tokens for authentication
     * and authorization, utilizing a custom JWT authentication converter for role-based access control.
     *
     * @param http the {@link HttpSecurity} to configure
     * @return the configured {@link SecurityFilterChain}
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(withDefaults())
                .exceptionHandling(eH -> eH.authenticationEntryPoint(new RestAuthenticationEntryPoint()))
                .authorizeHttpRequests(
                        auth -> {
                            //allows login/register
                            auth.requestMatchers("/api/v1/auth/**").permitAll();

                            //secure the rest of the API
                            auth.requestMatchers("/api/v1/**").authenticated();

                            //docs etc...
                            auth.anyRequest().permitAll();
                        }
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .oauth2ResourceServer(auth -> auth.jwt(jwtConfigurer -> {
                    var jwtConverter = new JwtAuthenticationConverter();
                    var jwtAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();
                    jwtAuthoritiesConverter.setAuthorityPrefix("");
                    jwtConverter.setJwtGrantedAuthoritiesConverter(jwtAuthoritiesConverter);
                    jwtConfigurer.jwtAuthenticationConverter(jwtConverter);
                }))
                .addFilterBefore(new JWTFilterChain(keyProperties), UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    /**
     * Configures a JWT decoder using the application's RSA public key.
     * <p>
     * Utilizes Nimbus JOSE + JWT to provide a JWT decoder bean capable of validating and parsing JWT tokens
     * based on the RSA public key defined in the application's configuration properties. This is crucial for
     * authenticating requests in a stateless fashion, ensuring that tokens are valid and issued by a trusted
     * authority.
     *
     * @return the configured {@link JwtDecoder}
     */
    @Bean
    JwtDecoder jwtDecoder() {
        return NimbusJwtDecoder.withPublicKey(keyProperties.publicKey()).build();
    }

    /**
     * Configures a JWT encoder using the application's RSA keys.
     * <p>
     * Sets up a JWT encoder capable of generating JWT tokens, leveraging the RSA private key for signing.
     * The encoder supports creating tokens that adhere to the JWK (JSON Web Key) format, ensuring interoperability
     * and secure token issuance. This bean is essential for services that issue JWTs, such as during user
     * authentication processes.
     *
     * @return the configured {@link JwtEncoder}
     */
    @Bean
    JwtEncoder jwtEncoder() {
        RSAKey rsaKey = new RSAKey.Builder(keyProperties.publicKey())
                .privateKey(keyProperties.privateKey())
                .build();

        //JSON Web Key (JWK) set. Represented by a JSON object that contains an array of JSON Web Keys
        var jwKeySet = new JWKSet(rsaKey);

        //JSON Web Key (JWK) source backed by an immutable JWK set.
        //the security context is used to pass additional parameters to the JWK source, such as the JWS algorithm restrictions
        JWKSource<SecurityContext> jwkSource = new ImmutableJWKSet<>(jwKeySet);

        //the encoder requires a JWKSource and a SecurityContext
        //finally we can create the encoder:
        return new NimbusJwtEncoder(jwkSource);
    }
}
