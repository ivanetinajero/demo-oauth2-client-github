package dev.itinajero.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import dev.itinajero.app.model.Usuario;
import dev.itinajero.app.service.IUsuariosService;

@Controller
@RequestMapping("/usuarios")
public class UsuariosController {

    @Autowired
    private IUsuariosService usuariosService;

    @GetMapping
    public String mostrarListado(@RequestParam(defaultValue = "0") int page, Model model) {
        Pageable pageable = PageRequest.of(page, 10,Sort.by("id").descending());
        Page<Usuario> usuarios = usuariosService.buscarTodos(pageable);
        model.addAttribute("usuarios", usuarios);
        return "usuarios/lista";
    }

    @GetMapping("/crear")
    public String crearUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "usuarios/formulario-editar";
    }

    @PostMapping("/guardar")
    public String guardarUsuario(@ModelAttribute Usuario usuario, RedirectAttributes attributes) {
        if (usuario.getPerfiles() == null || usuario.getPerfiles().isEmpty()) {
            attributes.addFlashAttribute("msgError", "Debes asignar al menos un perfil al usuario.");
            return "redirect:/usuarios/crear";
        }
        usuariosService.guardar(usuario);
        attributes.addFlashAttribute("msg", "Usuario guardado correctamente.");
        return "redirect:/usuarios";
    }

    @GetMapping("/editar/{id}")
    @ResponseBody
    public Usuario editarUsuario(@PathVariable("id") Integer id) {
        return usuariosService.buscarPorId(id);
    }

    @PostMapping("/eliminar")
    public String eliminarUsuarioPost(@RequestParam("id") Integer id, RedirectAttributes attributes) {
        usuariosService.eliminar(id);
        attributes.addFlashAttribute("msg", "Usuario eliminado correctamente.");
        return "redirect:/usuarios";
    }

}
