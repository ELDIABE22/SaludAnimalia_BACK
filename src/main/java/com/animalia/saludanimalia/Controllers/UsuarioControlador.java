package com.animalia.saludanimalia.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.animalia.saludanimalia.Models.Usuario;
import com.animalia.saludanimalia.Models.response.ApiResponse;
import com.animalia.saludanimalia.Models.response.AuthResponse;
import com.animalia.saludanimalia.Services.UsuarioServicio;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class UsuarioControlador {
    @Autowired
    UsuarioServicio usuarioService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> inicioSesionController(@RequestBody Usuario usuario) {
        ApiResponse<AuthResponse> response = usuarioService.iniciarSesionServicio(usuario);

        if (response.getData() != null) {
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Usuario>> registroUsuarioControlador(@RequestBody Usuario usuario) {
        ApiResponse<Usuario> response = usuarioService.registroUsuarioServicio(usuario);

        HttpStatus status = "Usuario registrado".equals(response.getMessage())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ApiResponse<Usuario>> actualizarUsuarioControlador(@PathVariable Integer id,
            @RequestBody Usuario usuario) {
        ApiResponse<Usuario> response = usuarioService.actualizarUsuarioServicio(id, usuario);

        HttpStatus status = "Usuario actualizado exitosamente".equals(response.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarUsuarioControlador(@PathVariable Integer id) {
        ApiResponse<Void> response = usuarioService.eliminarUsuarioServicio(id);

        HttpStatus status = "Usuario eliminado exitosamente".equals(response.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }
}
