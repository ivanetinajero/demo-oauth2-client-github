package dev.itinajero.app.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import dev.itinajero.app.service.CustomOAuth2UserService;
import dev.itinajero.app.service.StrictOAuth2UserService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Servicio personalizado que procesa la información del usuario autenticado por OAuth2.
     * Permite adaptar los datos recibidos del proveedor externo (Google, GitHub, etc.) a la lógica y modelo de usuarios de la aplicación.
     * Es inyectado en la configuración de seguridad para personalizar el flujo de autenticación.
     */
    
    /* 
    private final CustomOAuth2UserService oAuth2UserService;

    public SecurityConfig(CustomOAuth2UserService oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }
    */

    private final StrictOAuth2UserService oAuth2UserService;

    public SecurityConfig(StrictOAuth2UserService oAuth2UserService) {
        this.oAuth2UserService = oAuth2UserService;
    }

        /**
         * Configuración de seguridad principal de la aplicación.
         * Si el usuario no está autenticado, Spring Security intercepta la petición
         * y redirige automáticamente al proveedor OAuth2 configurado (por ejemplo, Google, GitHub, etc.).
         * Esto se gestiona en la sección .oauth2Login() de la configuración.
         * Los endpoints públicos y protegidos se definen en authorizeHttpRequests.
         */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                    .requestMatchers(HttpMethod.GET, "/", "/acerca").permitAll()
                    .requestMatchers("/productos/eliminar/**").hasAnyAuthority("ADMIN")
                    .requestMatchers("/productos/**").hasAnyAuthority("ADMIN", "SUPERVISOR")
                    .requestMatchers("/usuarios/**").hasAnyAuthority("ADMIN")
                    .anyRequest().authenticated())

            // Si el usuario no está autenticado, se redirige automáticamente al proveedor OAuth2
            .oauth2Login(oauth2 -> oauth2.userInfoEndpoint(userInfo -> userInfo.userService(oAuth2UserService)))

            .logout(logout -> logout
                    .logoutUrl("/logout") // endpoint para cerrar sesión
                    .logoutSuccessUrl("/") // redirigir después del logout
                    .invalidateHttpSession(true)
                    .clearAuthentication(true)
                    .deleteCookies("JSESSIONID"));

        return http.build();
    }

}
