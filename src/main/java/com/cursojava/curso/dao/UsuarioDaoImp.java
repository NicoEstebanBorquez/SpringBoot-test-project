package com.cursojava.curso.dao;

import com.cursojava.curso.models.Usuario;
import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;


import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository
@Transactional
public class UsuarioDaoImp implements UsuarioDao {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Usuario> getUsuarios() {
        String query = "FROM Usuario";
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public void eliminar(Long id) {
        Usuario usuario = entityManager.find(Usuario.class, id);
        entityManager.remove(usuario);
    }

    @Override
    public void registrar(Usuario usuario) {
        entityManager.merge(usuario);
    }

    @Override
    public Usuario obtenerUsuarioPorCredenciales(Usuario usuario) {

        /*
        //CÓDIGO PREVIO A LA UTILIZACIÓN DE ARGON2 PARA ENCRIPTAR LA CONTRASEÑA.
        EN ESTA VERSIÓN SE BUSCABA EN LA BD UN USUARIO POR SU EMAIL Y CONTRASEÑA:

        String query = "FROM Usuario WHERE email = :email AND password = :password";
        List<Usuario> lista = entityManager.createQuery(query)
                .setParameter("email", usuario.getEmail())
                .setParameter("password", usuario.getPassword())
                .getResultList();

        if(lista.isEmpty()){
            return false;
        } else {
            return true;
        }*/

        String query = "FROM Usuario WHERE email = :email";
        List<Usuario> lista = entityManager.createQuery(query)
                .setParameter("email", usuario.getEmail())
                .getResultList();

        //Si introducimos un email que no está en la BD en la siguiente línea, al intentar obtener el password
        // nos daría un NullPointerException, porque la lista estaría vacía. Eso se arregla con:
        if (lista.isEmpty()) {
            return null;
        }

        String passwordHasheada = lista.get(0).getPassword();
        Argon2 argon2 = Argon2Factory.create(Argon2Factory.Argon2Types.ARGON2id);

        //Se compara la contraseña introducida por el usuario con la contrseña almacenada en la BD
        if (argon2.verify(passwordHasheada, usuario.getPassword())) {
            return lista.get(0);
        }
        return null;
    }
}
