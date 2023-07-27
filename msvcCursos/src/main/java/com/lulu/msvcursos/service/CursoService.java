package com.lulu.msvcursos.service;

import com.lulu.msvcursos.models.Usuario;
import com.lulu.msvcursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    List<Curso> findAll();
    Optional<Curso> findById(Long id);
    Optional<Curso> findByIdConUsuarios(Long id);
    Curso save(Curso c);
    void delete(Long id);
    void deleteCursoUsuarioById(Long id);
    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId);
    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId);
    Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId);
}