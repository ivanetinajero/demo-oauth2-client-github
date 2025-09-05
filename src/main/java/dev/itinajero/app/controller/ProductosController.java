package dev.itinajero.app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
import dev.itinajero.app.model.Producto;
import dev.itinajero.app.service.IProductosService;

@Controller
@RequestMapping("/productos")
public class ProductosController {

   @Autowired
   private IProductosService productosService;

   @GetMapping
   public String listar(@RequestParam(defaultValue = "0") int page, Model model) {
      Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
      Page<Producto> productos;
      productos = productosService.buscarTodos(pageable);
      model.addAttribute("productos", productos);
      return "productos/lista";
   }

   @PostMapping("/guardar")
   public String guardar(@ModelAttribute Producto producto) {
   productosService.guardar(producto);
   return "redirect:/productos?msg=Producto guardado correctamente.";
   }

   @GetMapping("/editar/{id}")
   @org.springframework.web.bind.annotation.ResponseBody
   public Producto editar(@PathVariable int id) {
      return productosService.buscarPorId(id);
   }

   @PostMapping("/actualizar")
   public String actualizar(@ModelAttribute Producto producto) {
   productosService.guardar(producto);
   return "redirect:/productos?msg=Producto actualizado correctamente.";
   }

   @PostMapping("/eliminar")
   public String eliminar(@RequestParam int id) {
   productosService.eliminar(id);
   return "redirect:/productos?msg=Producto eliminado correctamente.";
   }

   @GetMapping("/nuevo")
   public String nuevo(Model model) {
      model.addAttribute("producto", new Producto());
      return "productos/formulario";
   }

   @GetMapping("/buscar")
   public String buscar(@RequestParam(required = false) String nombre,@RequestParam(required = false) String descripcion, @RequestParam(required = false) Double precio,
         @RequestParam(required = false) Integer cantidad, @RequestParam(defaultValue = "0") int page, Model model) {
      Producto filtro = new Producto();
      if (nombre != null && !nombre.isEmpty())
         filtro.setNombre(nombre);
      if (descripcion != null && !descripcion.isEmpty())
         filtro.setDescripcion(descripcion);
      if (precio != null)
         filtro.setPrecio(precio);
      if (cantidad != null)
         filtro.setCantidad(cantidad);
      // Evitar filtrar por fechaCreacion
      filtro.setFechaCreacion(null);

      ExampleMatcher matcher = ExampleMatcher.matching()
            .withIgnoreNullValues()
            .withMatcher("nombre", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase())
            .withMatcher("descripcion", ExampleMatcher.GenericPropertyMatchers.contains().ignoreCase());
      Example<Producto> example = Example.of(filtro, matcher);
      Pageable pageable = PageRequest.of(page, 10, Sort.by("id").descending());
      Page<Producto> productos = productosService.buscarTodos(example, pageable);
      model.addAttribute("productos", productos);
      return "productos/lista";
   }

}
