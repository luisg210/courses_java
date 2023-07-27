package com.lulu.msvcusers.controller;

import com.lulu.msvcusers.model.entity.Usuario;
import com.lulu.msvcusers.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UsuarioController {

    @Autowired
    private UsuarioService service;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @GetMapping("/")
    public Map<String, List<Usuario>> findAll() {

        return Collections.singletonMap("Users", this.service.findAll());
    }

    @GetMapping("/authorized")
    public Map<String, Object> authorized(@RequestParam(name = "code") String code) {

        return Collections.singletonMap("code", code);
    }

    @GetMapping("/login")
    public ResponseEntity<?> loginByEmail(@RequestParam String email) {
        Optional<Usuario> user = service.byEmail(email);

        if (user.isPresent()) {
            return ResponseEntity.ok(user.get());
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Usuario> oUsuario = this.service.findById(id);
        if (oUsuario.isPresent()) {
            return ResponseEntity.ok(oUsuario.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> save(@Valid @RequestBody Usuario u, BindingResult result) {
        if (result.hasErrors()) {
            return validate(result);
        }

        if (!u.getEmail().isEmpty() && this.service.existsByEmail(u.getEmail())) {
            return ResponseEntity.badRequest()
                    .body(Collections.singletonMap("mensaje",
                            "Ya existe un usuario con ese email"));
        }

        u.setPassword(passwordEncoder.encode(u.getPassword()));

        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(u));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Usuario usuario, @PathVariable Long id, BindingResult result) {
        if (result.hasErrors()) {
            return validate(result);
        }

        Optional<Usuario> oUsuario = this.service.findById(id);
        if (oUsuario.isPresent()) {
            Usuario usuarioDb = oUsuario.get();

            if (!usuario.getEmail().isEmpty() && !usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail()) &&
                    this.service.byEmail(usuario.getEmail()).isPresent()) {
                return ResponseEntity.badRequest()
                        .body(Collections.singletonMap("mensaje",
                                "Ya existe un usuario con ese email"));
            }

            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(passwordEncoder.encode(usuario.getPassword()));

            return ResponseEntity.status(HttpStatus.OK).body(this.service.save(usuarioDb));
        }

        return ResponseEntity.notFound().build();
    }



    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Usuario> oUsuario = this.service.findById(id);
        if (oUsuario.isPresent()) {
            this.service.delete(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios_por_cursos")
    public ResponseEntity<?> findAllByCursos(@RequestParam List<Long> ids) {

        return ResponseEntity.ok(this.service.listarPorId(ids));
    }

    private ResponseEntity<?> validate(BindingResult result) {
        if (result.hasErrors()) {
            Map<String, String> errors = new HashMap<>();
            result.getFieldErrors().forEach(error -> {
                errors.put(error.getField(), "El campo " + error.getField() + " " + error.getDefaultMessage());
            });

            return ResponseEntity.badRequest().body(errors);
        }
        return null;
    }
}