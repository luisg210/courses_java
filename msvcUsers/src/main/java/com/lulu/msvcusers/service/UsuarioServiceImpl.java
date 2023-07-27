package com.lulu.msvcusers.service;

import com.lulu.msvcusers.clients.CursoClientRest;
import com.lulu.msvcusers.model.entity.Usuario;
import com.lulu.msvcusers.repositories.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository repository;
    @Autowired
    private CursoClientRest clientRest;

    @Override
    @Transactional(readOnly = true)
    public List<Usuario> findAll() {
        return (List<Usuario>) this.repository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Usuario> findById(Long id) {
        return this.repository.findById(id);
    }

    @Override
    @Transactional
    public Usuario save(Usuario u) {
        return this.repository.save(u);
    }

    @Override
    @Transactional
    public void delete(Long id) {
        this.repository.deleteById(id);
        this.clientRest.deleteCursoUsuarioPorId(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return this.repository.existsByEmail(email);
    }

    @Override
    public Optional<Usuario> byEmail(String email) {
        return this.repository.byEmail(email);
    }

    @Override
    public List<Usuario> listarPorId(Iterable<Long> ids) {
        return (List<Usuario>) this.repository.findAllById(ids);
    }
}