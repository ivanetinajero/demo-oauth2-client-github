package dev.itinajero.app.service;

import java.util.List;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import dev.itinajero.app.model.Perfil;
import dev.itinajero.app.model.Usuario;

public interface IUsuariosService {
   
   void asignarPerfiles(Integer idUsuario, List<Integer> perfiles);
   Page<Usuario> buscarTodos(Pageable pageable);
   Page<Usuario> buscarTodos(Example<Usuario> example, Pageable pageable);
   void guardar(Usuario usuario);
   Usuario buscarPorId(int idUsuario);
   void eliminar(int idProducto);
   List<Perfil> buscarPerfiles();

}
