package edu.mlm.ecommercestore.config.webConfig;

import edu.mlm.ecommercestore.validator.AllowedParameters;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ParameterValidationInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull Object handler
    ) {
        if (handler instanceof HandlerMethod handlerMethod) {
            AllowedParameters allowedParameters = handlerMethod.getMethodAnnotation(AllowedParameters.class);
            if (allowedParameters != null) {
                Set<String> allowedParamSet = Arrays.stream(allowedParameters.value()).collect(Collectors.toSet());
                request.getParameterMap().keySet().forEach(param -> {
                    if (!allowedParamSet.contains(param)) {
                        throw new IllegalArgumentException("Unexpected parameter: " + param);
                    }
                });
            }
        }
        return true;
    }
}