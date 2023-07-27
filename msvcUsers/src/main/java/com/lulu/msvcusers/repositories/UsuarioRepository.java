package com.lulu.msvcusers.repositories;

import com.lulu.msvcusers.model.entity.Usuario;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UsuarioRepository extends CrudRepository<Usuario, Long> {

    boolean existsByEmail(String email);

    @Query("select u from Usuario u where u.email = ?1")
    Optional<Usuario> byEmail(String email);

}