package edu.mlm.ecommercestore.security;


import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import java.io.IOException;

/**
 * Custom implementation of {@link AuthenticationEntryPoint} used to commence authentication schemes.
 * This class is responsible for sending a 401 Unauthorized error response to the client when an
 * authentication exception is caught, indicating that the client must authenticate to access the
 * requested resource.
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint {
    /**
     * Commences an authentication scheme.
     * <p>
     * This method is called whenever an {@link AuthenticationException} is thrown indicating that
     * the user is not authenticated. It sends an HTTP 401 error response indicating that
     * authentication is required to access the resource.
     * </p>
     *
     * @param request       the servlet request that resulted in an {@link AuthenticationException}
     * @param response      the servlet response, containing the 401 error message
     * @param authException the exception that led to the commencement of the authentication process
     * @throws IOException      if an input or output exception occurs
     * @throws ServletException if a servlet exception occurs
     */
    @Override
    public void commence(HttpServletRequest request,
                         HttpServletResponse response,
                         AuthenticationException authException) throws IOException, ServletException {
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "You must to be authenticated to perform this action.");
    }
}
