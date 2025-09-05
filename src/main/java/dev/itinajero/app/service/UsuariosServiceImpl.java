package dev.itinajero.app.service;

import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import dev.itinajero.app.model.Perfil;
import dev.itinajero.app.model.Usuario;
import dev.itinajero.app.repository.IPerfilesRepository;
import dev.itinajero.app.repository.IUsuariosRepository;

@Service
public class UsuariosServiceImpl implements IUsuariosService {

   @Autowired
   private IUsuariosRepository usuariosRepository;

   @Autowired
   private IPerfilesRepository perfilesRepository;

   @Override
   public Page<Usuario> buscarTodos(Pageable pageable) {
      return usuariosRepository.findAll(pageable);
   }

   public Page<Usuario> buscarTodos(Example<Usuario> example, Pageable pageable) {
      return usuariosRepository.findAll(example, pageable);
   }

   @Override
   public void guardar(Usuario usuario) {
      usuariosRepository.save(usuario);
   }

   @Override
   public Usuario buscarPorId(int idUsuario) {
      Optional<Usuario> optional = usuariosRepository.findById(Long.valueOf(idUsuario));
      return optional.orElse(null);
   }

   @Override
   public void eliminar(int idUsuario) {
      usuariosRepository.deleteById(Long.valueOf(idUsuario));
   }

   @Override
   public void asignarPerfiles(Integer idUsuario, List<Integer> perfilesIds) {
      Usuario usuario = buscarPorId(idUsuario);
      if (usuario != null) {
         java.util.Set<Perfil> nuevosPerfiles = new java.util.HashSet<>();
         for (Integer idPerfil : perfilesIds) {
            Perfil perfil = new Perfil();
            perfil.setId(Long.valueOf(idPerfil));
            nuevosPerfiles.add(perfil);
         }
         usuario.setPerfiles(nuevosPerfiles);
         guardar(usuario);
      }
   }

   @Override
   public List<Perfil> buscarPerfiles() {
      return perfilesRepository.findAll();
   }

}