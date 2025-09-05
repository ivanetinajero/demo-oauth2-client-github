// productos-ajax.js
$(document).ready(function() {
    // Cargar datos en modal de editar por AJAX y llenar el formulario
    $('#modalEditar').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var id = button.data('id');
        // Limpiar el formulario antes de llenarlo
        $('#formEditar')[0].reset();
        // Obtener datos del producto
        $.get('/productos/editar/' + id, function(data) {
            $('#editarId').val(data.id);
            $('#nombreEditar').val(data.nombre);
            $('#descripcionEditar').val(data.descripcion);
            $('#precioEditar').val(data.precio);
            $('#cantidadEditar').val(data.cantidad);
        });
    });
    // Pasar id al modal de eliminar
    $('#modalEliminar').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var id = button.data('id');
        $('#eliminarId').val(id);
    });
    
});
