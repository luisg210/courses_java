package com.lulu.msvcursos.clients;

import com.lulu.msvcursos.models.Usuario;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@FeignClient(name = "msvc-users", url = "msvc-users:8081/usuarios")
public interface UsuarioClientRest {

    @GetMapping("/{id}")
    Usuario detalle(@PathVariable Long id);
    @PostMapping("/")
    Usuario crear(@RequestBody Usuario usuario);

    @GetMapping("/usuarios_por_cursos/")
    List<Usuario> obtenerUsuariosPorCurso(@RequestParam Iterable<Long> ids);
}