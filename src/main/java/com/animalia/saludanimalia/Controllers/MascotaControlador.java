package com.animalia.saludanimalia.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.animalia.saludanimalia.Models.response.ApiResponse;
import com.animalia.saludanimalia.Models.response.MascotaResponse;
import com.animalia.saludanimalia.Services.MascotaServicio;
import com.animalia.saludanimalia.Models.Mascota;
import java.util.List;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MascotaControlador {

    @Autowired
    MascotaServicio mascotaServicio;

    @PostMapping("/pet/new")
    public ResponseEntity<ApiResponse<MascotaResponse>> agregarMascotaController(@RequestBody Mascota mascota) {
        ApiResponse<MascotaResponse> response = mascotaServicio.agregarMascotaServicio(mascota);

        HttpStatus status = "Mascota agregada exitosamente".equals(response.getMessage())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }

    @PutMapping("/pet/update")
    public ResponseEntity<ApiResponse<MascotaResponse>> actualizarMascotaController(@RequestBody Mascota mascota) {
        ApiResponse<MascotaResponse> response = mascotaServicio.actualizarMascotaServicio(mascota);

        HttpStatus status = "Mascota actualizada exitosamente".equals(response.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/pet/{idUsuario}")
    public ResponseEntity<List<Mascota>> obtenerMascotasDeUsuarioController(@PathVariable Integer idUsuario) {
        List<Mascota> response = mascotaServicio.obtenerMascotasDeUsuario(idUsuario);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/pet/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarMascotaController(@PathVariable Integer id) {
        ApiResponse<Void> response = mascotaServicio.eliminarMascota(id);

        HttpStatus status = "Mascota eliminada exitosamente".equals(response.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }
}
