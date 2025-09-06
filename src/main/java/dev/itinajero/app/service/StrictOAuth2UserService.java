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
import dev.itinajero.app.model.Usuario;
import dev.itinajero.app.repository.IUsuariosRepository;

/**
 * Implementación alternativa de OAuth2UserService.
 * Permite que solo los usuarios previamente registrados en la base de datos local puedan autenticarse.
 * Si el usuario autenticado por GitHub no existe en el catálogo, se rechaza el acceso y NO se crea el usuario automáticamente.
 * 
 * Flujo:
 * 1. Obtiene la información del usuario autenticado desde GitHub.
 * 2. Extrae el login (nombre de usuario público) de GitHub.
 * 3. Busca el usuario en la base de datos local usando el login.
 *    - Si no existe, lanza una excepción y el acceso es denegado.
 * 4. Si existe, construye la lista de roles/perfiles y retorna el objeto OAuth2User para la sesión.
 */
@Service // Descomenta esta anotación si quieres usar esta implementación
public class StrictOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

   private final IUsuariosRepository usuariosRepository;

   public StrictOAuth2UserService(IUsuariosRepository usuariosRepository) {
      this.usuariosRepository = usuariosRepository;
   }

   @Override
   public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

      // 1. Obtener la información del usuario autenticado desde el proveedor OAuth2 (GitHub)
      OAuth2User user = new DefaultOAuth2UserService().loadUser(userRequest);

      // 2. Extraer el login de GitHub
      String githubLogin = user.getAttribute("login");

      // 3. Buscar el usuario en la base de datos local usando el login de GitHub
      Usuario usuario = usuariosRepository.findByGithubLoginAndEstatus(githubLogin, 1)
            .orElseThrow(() ->
            // Si el usuario no existe, se rechaza el acceso lanzando una excepción
            new OAuth2AuthenticationException("Acceso denegado: el usuario no está registrado en el catálogo local."));

      // 4. Construir la lista de roles/perfiles (authorities) para el usuario autenticado
      List<GrantedAuthority> authorities = usuario.getPerfiles().stream()
            .map(r -> new SimpleGrantedAuthority(r.getPerfil()))
            .map(a -> (GrantedAuthority) a)
            .toList();

      // 5. Retornar el objeto OAuth2User con los atributos y roles para la sesión
      return new DefaultOAuth2User(authorities, user.getAttributes(), "login");
      
   }

}
