package com.cursojava.curso.controllers;

import com.cursojava.curso.dao.UsuarioDao;
import com.cursojava.curso.models.Usuario;
import com.cursojava.curso.utils.JWTUtil;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private JWTUtil jwtUtil;

    private boolean validarToken(String token) {
        String usuarioID = jwtUtil.getKey(token);

        return usuarioID != null;
    }

    @RequestMapping(value = "api/usuarios/{id}")
    public Usuario getUsuario(@PathVariable long id) {
        Usuario usuario = new Usuario();
        usuario.setId(id);
        usuario.setNombre("Nico");
        usuario.setApellido("Esteban");
        usuario.setEmail("nico@mail.com");
        usuario.setTelefono("670340130");
        usuario.setPassword("1g04da6sd4");

        return usuario;
    }

    @RequestMapping(value = "api/usuarios", method = RequestMethod.GET)
    public List<Usuario> getUsuarios(@RequestHeader(value = "Authorization") String token) {
        if (!validarToken(token)) {
            return null;
        }

        return usuarioDao.getUsuarios();
    }

    @RequestMapping(value = "api/usuarios", method = RequestMethod.POST)
    public void registrarUsuario(@RequestBody Usuario usuario) {

        //ENCRIPTACION DE LA CONTRASEÑA CON LIBRERÍA ARGON2:
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);
        //Encriptacion: nº de iteraciones (veces que encripta), uso de memoria, nº de hilos de proceso, texto a encriptar
        String hash = argon2.hash(1, 1024, 1, usuario.getPassword());
        //Se settea la contraseña con el hash encriptado y se almacena en la BD
        usuario.setPassword(hash);

        usuarioDao.registrar(usuario);
    }

    @RequestMapping(value = "api/usuarios/{id}", method = RequestMethod.DELETE)
    public void eliminar(@RequestHeader(value = "Authorization") String token, @PathVariable Long id) {
        if (!validarToken(token)) {
            return;
        }
        usuarioDao.eliminar(id);
    }
}
