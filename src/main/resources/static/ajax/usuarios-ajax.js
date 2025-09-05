// usuarios-ajax.js
$(document).ready(function() {
    // Cargar datos en modal de editar por AJAX y llenar el formulario
    $('#modalEditarUsuario').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var id = button.data('id');
        // Limpiar el formulario antes de llenarlo
        $('#formEditarUsuario')[0].reset();
        // Obtener datos del usuario
        $.get('/usuarios/editar/' + id, function(data) {
            $('#editarUsuarioId').val(data.id);
            $('#editarNombreCompleto').val(data.nombreCompleto);
            $('#editarEmail').val(data.email);
            $('#editarEstatus').val(data.estatus);
            $('#editarGithubLogin').val(data.githubLogin);
            // Perfiles: limpiar y seleccionar los que correspondan
            $('#editarPerfiles option').prop('selected', false);
            if (data.perfiles && Array.isArray(data.perfiles)) {
                data.perfiles.forEach(function(perfil) {
                    $('#editarPerfiles option[value="' + perfil.id + '"]').prop('selected', true);
                });
            }
        });
    });
    // Pasar id al modal de eliminar
    $('#modalEliminarUsuario').on('show.bs.modal', function (event) {
        var button = $(event.relatedTarget);
        var id = button.data('id');
        $('#eliminarUsuarioId').val(id);
    });
    
});
