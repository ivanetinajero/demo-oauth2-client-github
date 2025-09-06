package dev.itinajero.app.service;

import java.util.List;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import dev.itinajero.app.model.Perfil;
import dev.itinajero.app.model.Usuario;
import dev.itinajero.app.repository.IPerfilesRepository;
import dev.itinajero.app.repository.IUsuariosRepository;

/**
 * Servicio personalizado que procesa la información del usuario autenticado por OAuth2.
 * Spring Security invoca este servicio después de recibir el token de autenticación del proveedor (GitHub).
 * Aquí se extraen los datos del usuario (login, nombre, email) y se adaptan al modelo de la aplicación.
 * Si el usuario no existe en la base de datos, se crea automáticamente y se le asigna el perfil USUARIO.
 * Finalmente, se construye el objeto OAuth2User con los roles/perfiles correspondientes para la sesión.
 */
//@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final IUsuariosRepository usuariosRepository;
    private final IPerfilesRepository perfilesRepository;

    public CustomOAuth2UserService(IUsuariosRepository usuariosRepository, IPerfilesRepository perfilesRepository) {
        this.usuariosRepository = usuariosRepository;
        this.perfilesRepository = perfilesRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        
        // 1. Obtener la información del usuario autenticado desde el proveedor OAuth2 (GitHub)
        OAuth2User user = new DefaultOAuth2UserService().loadUser(userRequest);

        // 2. Extraer los atributos principales del usuario desde la respuesta JSON de GitHub
        // "login" es el identificador público de GitHub
        String githubLogin = user.getAttribute("login");
        // "name" es el nombre completo del usuario (puede ser null si no lo tiene configurado)
        String nombre = user.getAttribute("name");
        // "email" es el correo electrónico principal (puede ser null si el usuario no lo comparte)
        String email = user.getAttribute("email");

        // 3. Buscar el usuario en la base de datos local usando el login de GitHub. Si no existe, se crea automáticamente y se le asigna el perfil USUARIO
        Usuario usuario = usuariosRepository.findByGithubLoginAndEstatus(githubLogin, 1)
            .orElseGet(() -> {
                // Crear nuevo usuario local
                Usuario nuevo = new Usuario();
                nuevo.setGithubLogin(githubLogin);
                nuevo.setNombreCompleto(nombre);
                nuevo.setEmail(email);
                // Asignar perfil por defecto USUARIO
                Perfil rolUsuario = perfilesRepository.findByPerfil("USUARIO")
                    .orElseThrow(() -> new RuntimeException("Perfil USUARIO no encontrado"));
                nuevo.getPerfiles().add(rolUsuario);
                // Guardar el nuevo usuario en la base de datos
                return usuariosRepository.save(nuevo);
            });

        // 4. Construir la lista de roles/perfiles (authorities) para el usuario autenticado. Cada perfil se convierte en una autoridad de Spring Security
        List<GrantedAuthority> authorities = usuario.getPerfiles().stream()
            // Si quieres usar el prefijo "ROLE_", descomenta la siguiente línea:
            //.map(r -> new SimpleGrantedAuthority("ROLE_" + r.getPerfil()))
            .map(r -> new SimpleGrantedAuthority(r.getPerfil()))
            .map(a -> (GrantedAuthority) a)
            .toList();

        // 5. Retornar el objeto OAuth2User con los atributos y roles para la sesión. // "login" será el identificador principal del usuario en la sesión
        return new DefaultOAuth2User(authorities, user.getAttributes(), "login");

    }

}
