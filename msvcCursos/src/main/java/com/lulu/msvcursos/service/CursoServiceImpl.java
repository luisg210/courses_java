package com.lulu.msvcursos.service;

import com.lulu.msvcursos.clients.UsuarioClientRest;
import com.lulu.msvcursos.models.Usuario;
import com.lulu.msvcursos.models.entity.Curso;
import com.lulu.msvcursos.models.entity.CursoUsuario;
import com.lulu.msvcursos.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CursoServiceImpl implements CursoService {

    @Autowired
    private CursoRepository repository;
    @Autowired
    private UsuarioClientRest clientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Curso> findAll() {
        return (List<Curso>) this.repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> findById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Curso> findByIdConUsuarios(Long id) {
        Optional<Curso> o = this.repository.findById(id);
        if (o.isPresent()) {
            Curso curso = o.get();
            if (!curso.getCursoUsuarios().isEmpty()) {
                List<Long> ids = curso.getCursoUsuarios().stream().map(cu ->
                    cu.getUsuarioId()).collect(Collectors.toList());

                List<Usuario> usuarios = this.clientRest.obtenerUsuariosPorCurso(ids);
                curso.setUsuarios(usuarios);
            }

            return Optional.of(curso);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Curso save(Curso c) {
        return this.repository.save(c);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.repository.deleteById(id);
    }

    @Override
    @Transactional
    public void deleteCursoUsuarioById(Long id) {
        this.repository.eliminarCursoUsuarioId(id);
    }

    @Override
    @Transactional
    public Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = this.repository.findById(cursoId);
        if (o.isPresent()) {
            Usuario usuarioMsvc = this.clientRest.detalle(usuario.getId());

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuario.getId());

            curso.addCursoUsuario(cursoUsuario);
            this.repository.save(curso);

            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = this.repository.findById(cursoId);
        if (o.isPresent()) {
            Usuario usuarioMsvc = this.clientRest.crear(usuario);

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuarioMsvc.getId());

            curso.addCursoUsuario(cursoUsuario);
            this.repository.save(curso);

            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId) {
        Optional<Curso> o = this.repository.findById(cursoId);
        if (o.isPresent()) {
            Usuario usuarioMsvc = this.clientRest.detalle(usuario.getId());

            Curso curso = o.get();
            CursoUsuario cursoUsuario = new CursoUsuario();
            cursoUsuario.setUsuarioId(usuario.getId());

            curso.removeCursoUsuario(cursoUsuario);
            this.repository.save(curso);

            return Optional.of(usuarioMsvc);
        }

        return Optional.empty();
    }
}