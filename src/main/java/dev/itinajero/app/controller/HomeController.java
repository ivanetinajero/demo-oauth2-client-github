package dev.itinajero.app.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    Logger log = LogManager.getLogger(HomeController.class);

    @GetMapping("/")
    public String home(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            // El usuario ya inició sesión
            String username = authentication.getName(); // login de GitHub o username en tu tabla
            log.info("Usuario autenticado: " + username);
            // Roles del usuario
            var roles = authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList();
            log.info("Roles del usuario: " + roles);
            model.addAttribute("username", username);
            model.addAttribute("roles", roles);
        }
        return "home";
    }

    @GetMapping("/acerca")
    public String acerca() {
        return "acerca";
    }

}
