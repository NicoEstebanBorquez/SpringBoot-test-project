// Call the dataTables jQuery plugin
$(document).ready(function() {
  cargarUsuarios();
  $('#usuarios').DataTable();

  //Mostrar mail del usuario en topbar
    actualizarEmailUsuario();
});

function actualizarEmailUsuario(){
    document.getElementById('txt-email-usuario').outerHTML = localStorage.email;
}

//Función que devuelve un String con el Header
/*
function getHeader(){
    return{
            'Accept' : 'application/json',
            'Content-Type': 'application/json',
            'Authorization': localStorage.token
    };
}*/

async function cargarUsuarios() {
  const request = await fetch('api/usuarios', {
    method: 'GET',
    headers: {
    'Accept' : 'application/json',
    'Content-Type': 'application/json',
    'Authorization': localStorage.token
    }
  });
  const usuarios = await request.json();

  let listadoHTML = '';

  for(let usuario of usuarios){
    let botonEliminar = '<a href="#" onclick="eliminarUsuario('+usuario.id+')" class="btn btn-danger btn-circle btn-sm"><i class="fas fa-trash"></i></a>';
    let telefonoTxt = usuario.telefono == null ? '-' : usuario.telefono;

    let usuarioHTML = '<tr>'+
                        '<td>'+usuario.id+'</td>' +
                        '<td>'+usuario.nombre+' '+usuario.apellido+'</td>' +
                        '<td>'+usuario.email+'</td>' +
                        '<td>'+telefonoTxt+'</td>' +
                        '<td>'+botonEliminar+'</td>' +
                       '</tr>';
    listadoHTML += usuarioHTML;
  }
  document.querySelector('#usuarios tbody').outerHTML = listadoHTML;
}

async function eliminarUsuario(id){

    if(!confirm('¿Desea eliminar este usuario?')){
        return
    }

    const request = await fetch('api/usuarios/' + id, {
        method: 'DELETE',
        headers: {
        'Accept' : 'application/json',
        'Content-Type': 'application/json',
        'Authorization': localStorage.token
        }
      });

      //Recargar página
      location.reload();
}

