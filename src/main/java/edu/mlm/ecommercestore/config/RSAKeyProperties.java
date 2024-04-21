package edu.mlm.ecommercestore.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * Configuration properties for RSA key management within the application.
 * <p>
 * This record encapsulates the RSA public and private keys, allowing them to be configured externally
 * through application properties or YAML files. Leveraging Spring Boot's {@code @ConfigurationProperties},
 * it facilitates the easy injection and management of RSA key properties, enhancing the application's
 * security configuration.
 * <p>
 * The {@code prefix = "rsa"} annotation element indicates that the properties this record maps to in
 * the application configuration files should be prefixed with "rsa", allowing for structured and
 * readable configuration.
 *
 * @param publicKey  The RSA public key used for encrypting data or verifying signatures.
 * @param privateKey The RSA private key used for decrypting data or signing.
 */
@ConfigurationProperties(prefix = "rsa")
public record RSAKeyProperties(RSAPublicKey publicKey, RSAPrivateKey privateKey){}
