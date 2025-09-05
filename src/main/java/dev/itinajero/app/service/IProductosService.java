package dev.itinajero.app.service;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import dev.itinajero.app.model.Producto;

public interface IProductosService {

   Page<Producto> buscarTodos(Pageable pageable);
   Page<Producto> buscarTodos(Example<Producto> example, Pageable pageable);
   void guardar(Producto producto);
   Producto buscarPorId(int idProducto);
   void eliminar(int idProducto);

}
