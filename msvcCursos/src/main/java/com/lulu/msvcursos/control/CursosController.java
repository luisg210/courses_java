package com.lulu.msvcursos.control;

import com.lulu.msvcursos.models.Usuario;
import com.lulu.msvcursos.models.entity.Curso;
import com.lulu.msvcursos.service.CursoService;
import feign.FeignException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/cursos")
@CrossOrigin(origins = "*")
public class CursosController {

    @Autowired
    private CursoService service;

    @GetMapping("/")
    public List<Curso> findAll() {

        return this.service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {
        Optional<Curso> oCurso = this.service.findByIdConUsuarios(id);
        if (oCurso.isPresent()) {
            return ResponseEntity.ok(oCurso.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/")
    public ResponseEntity<?> save(@Valid @RequestBody Curso c, BindingResult result) {
        if (result.hasErrors()) {
            return validate(result);
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(c));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@Valid @RequestBody Curso curso, @PathVariable Long id, BindingResult result) {
        if (result.hasErrors()) {
            return validate(result);
        }

        Optional<Curso> oCurso = this.service.findById(id);
        if (oCurso.isPresent()) {
            Curso cursoDb = oCurso.get();
            cursoDb.setNombre(curso.getNombre());

            return ResponseEntity.status(HttpStatus.OK).body(this.service.save(cursoDb));
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {
        Optional<Curso> oCurso = this.service.findById(id);
        if (oCurso.isPresent()) {
            this.service.delete(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    @PutMapping("/asignar_usuario/{cursoId}")
    public ResponseEntity<?> asignarUsuario(@RequestBody Usuario usuario, @PathVariable
        Long cursoId) {
        Optional<Usuario> o;
        try {
            o = this.service.asignarUsuario(usuario, cursoId);

        } catch (FeignException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("msg", ex.getMessage()));
        }

        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }

        return ResponseEntity.notFound().build();
    }

    @PostMapping("/crear_usuario/{cursoId}")
    public ResponseEntity<?> crearUsuario(@RequestBody Usuario usuario, @PathVariable
    Long cursoId) {
        Optional<Usuario> o;
        try {
            o = this.service.crearUsuario(usuario, cursoId);

        } catch (FeignException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("msg", ex.getMessage()));
        }

        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(o.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar_usuario/{cursoId}")
    public ResponseEntity<?> eliminarUsuario(@RequestBody Usuario usuario, @PathVariable
    Long cursoId) {
        Optional<Usuario> o;
        try {
            o = this.service.eliminarUsuario(usuario, cursoId);

        } catch (FeignException ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Collections.singletonMap("msg", ex.getMessage()));
        }

        if (o.isPresent()) {
            return ResponseEntity.status(HttpStatus.OK).body(o.get());
        }

        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/eliminar-curso-usuario/{id}")
    public ResponseEntity<?> deleteCursoUsuario(@PathVariable Long id) {
        this.service.deleteCursoUsuarioById(id);


        return ResponseEntity.noContent().build();
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