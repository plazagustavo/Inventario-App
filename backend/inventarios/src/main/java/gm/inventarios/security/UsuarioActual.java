package gm.inventarios.security;

import gm.inventarios.user.model.User;
import gm.inventarios.user.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
public class UsuarioActual {

    @Autowired
    private UserRepository userRepository;

    public User obtener() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return null;
        }

        String email;
        if (authentication.getPrincipal() instanceof UserDetails) {
            email = ((UserDetails) authentication.getPrincipal()).getUsername();
        } else {
            email = authentication.getPrincipal().toString();
        }

        return userRepository.findByEmail(email).orElse(null);
    }

    public Integer obtenerId() {
        User user = obtener();
        return user != null ? user.getId() : null;
    }
}