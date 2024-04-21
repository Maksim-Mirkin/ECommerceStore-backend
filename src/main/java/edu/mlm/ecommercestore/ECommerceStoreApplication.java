package edu.mlm.ecommercestore;

import edu.mlm.ecommercestore.config.RSAKeyProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = {RSAKeyProperties.class})
public class ECommerceStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(ECommerceStoreApplication.class, args);
    }

}
