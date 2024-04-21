package edu.mlm.ecommercestore.dto.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDetailsResponseDTO {
    private String username;
    private Collection<? extends GrantedAuthority> roles;
}
