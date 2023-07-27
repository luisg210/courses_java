package com.lulu.msvcusers.service;

import com.lulu.msvcusers.model.entity.Usuario;

import java.util.List;
import java.util.Optional;

public interface UsuarioService {

    List<Usuario> findAll();
    Optional<Usuario> findById(Long id);
    Usuario save(Usuario u);
    void delete(Long id);
    boolean existsByEmail(String email);
    Optional<Usuario> byEmail(String email);
    List<Usuario> listarPorId(Iterable<Long> ids);

}