package com.animalia.saludanimalia.Repository;

import org.springframework.stereotype.Repository;

import com.animalia.saludanimalia.Models.Usuario;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

@Repository
public interface UsuarioRepositorio extends CrudRepository<Usuario, Integer> {
    Optional<Usuario> findByEmail(String email);

    Optional<Usuario> findByTelefono(String telefono);
}
