$(document).ready(function() {

});


async function iniciarSesion() {

    let datos = {};
    datos.email = document.getElementById('txtEmail').value;
    datos.password = document.getElementById('txtPassword').value;

  const request = await fetch('api/login', {
    method: 'POST',
    headers: {
    'Accept' : 'application/json',
    'Content-Type': 'application/json'
    },
    body: JSON.stringify(datos)
  });
  const respuesta = await request.text();

    //Se obtiene la respuesta del inicio de Sesión
  if(respuesta != 'FAIL'){
    //Si el token es distinto de FAIL (o error 401), se almacena el Token en el localstorage del Cliente
    localStorage.token = respuesta;
    localStorage.email = datos.email;

    //Redirección:
    window.location.href = 'usuarios.html';
  } else {
    alert("Las credenciales son incorrectas.");
  }
}
