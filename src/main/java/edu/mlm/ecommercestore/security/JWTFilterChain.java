package edu.mlm.ecommercestore.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.mlm.ecommercestore.config.RSAKeyProperties;
import edu.mlm.ecommercestore.dto.exception.ExceptionDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.val;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * Filter that executes once per request to validate JWT tokens in the authorization header.
 * Extends {@link OncePerRequestFilter} to ensure a single execution per dispatch.
 * Utilizes RSA keys for token validation specified by {@link RSAKeyProperties}.
 */
@RequiredArgsConstructor
public class JWTFilterChain extends OncePerRequestFilter {
    private final RSAKeyProperties keyProperties;

    /**
     * Filters each HTTP request to check for a valid JWT in the Authorization header, except for
     * requests to paths starting with "/api/v1/auth" and "/api/v1/products" which are allowed to bypass this security check.
     * If the token is valid, the filter chain continues to the next element. Invalid tokens or requests
     * without tokens result in a response with HTTP status 401 Unauthorized and a JSON error message.
     * <p>
     * This filter ensures that only authenticated users can access protected resources, while allowing
     * public access to authentication and registration endpoints.
     * </p>
     *
     * @param request     the servlet request
     * @param response    the servlet response
     * @param filterChain the filter chain
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs
     */
    @Override
    @SuppressWarnings("NullableProblems")
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (request.getRequestURI().startsWith("/api/v1/auth")) {
            filterChain.doFilter(request, response);
            return;
        }
        if (request.getRequestURI().startsWith("/api/v1/products")) {
            filterChain.doFilter(request, response);
            return;
        }
        try {
            val jwt = extractJwtFromRequest(request);
            validateToken(jwt);
        } catch (JWTVerificationException e) {
            val path = request.getRequestURI();
            val controller = transformURLToControllerName(path);
            val exception = ExceptionDTO.builder()
                    .controller(controller)
                    .controllerMethod("Contact Admin")
                    .method(request.getMethod())
                    .path(path)
                    .message(e.getMessage())
                    .status(HttpServletResponse.SC_UNAUTHORIZED)
                    .timestamp(LocalDateTime.now().toString())
                    .build();
            if (e.getMessage().equals("The token was expected to have 3 parts, but got 0.")) {
                exception.setMessage("Invalid token");
            }

            val objectMapper = new ObjectMapper();
            val json = objectMapper.writeValueAsString(exception.dtoToMap());
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(json);
        } finally {
            filterChain.doFilter(request, response);
        }
    }

    /**
     * Validates the provided JWT token using RSA256 algorithm.
     * <p>
     * Throws {@link JWTVerificationException} if token validation fails.
     * </p>
     *
     * @param token the JWT token to validate
     */
    private void validateToken(String token) throws JWTVerificationException {
        Algorithm algorithm = Algorithm.RSA256(
                keyProperties.publicKey(), keyProperties.privateKey()
        );
        JWTVerifier verifier = JWT.require(algorithm).build();
        verifier.verify(token);
    }

    /**
     * Extracts the JWT token from the Authorization header of the request.
     * Returns null if the header does not contain a properly formatted JWT.
     *
     * @param request the HTTP request to extract the token from
     * @return the JWT token or null if not present or incorrectly formatted
     */
    private String extractJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && (bearerToken.startsWith("Bearer ") || bearerToken.startsWith("bearer "))) {
            return bearerToken.substring(7);
        }
        return null;
    }

    /**
     * Transforms a request URL into a controller name.
     * This is used for logging or exception handling to identify the source of the request.
     *
     * @param url the request URL
     * @return a string representing the controller name derived from the URL
     */
    private String transformURLToControllerName(String url) {
        String[] parts = url.split("/");
        String resourceName = parts[3];
        if (resourceName.endsWith("s")) {
            resourceName = resourceName.substring(0, resourceName.length() - 1);
        }
        return resourceName.substring(0, 1).toUpperCase() + resourceName.substring(1) + "Controller"; // Returns "ProductController"
    }
}