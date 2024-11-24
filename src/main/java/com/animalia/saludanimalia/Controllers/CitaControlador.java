package com.animalia.saludanimalia.Controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.animalia.saludanimalia.Models.Cita;
import com.animalia.saludanimalia.Models.response.ApiResponse;
import com.animalia.saludanimalia.Models.response.CitaResponse;
import com.animalia.saludanimalia.Services.CitaServicio;

import lombok.RequiredArgsConstructor;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class CitaControlador {

    @Autowired
    private final CitaServicio citaServicio;

    @PostMapping("/appointment/new")
    public ResponseEntity<ApiResponse<CitaResponse>> registrarCitaController(@RequestBody Cita cita) {
        ApiResponse<CitaResponse> response = citaServicio.registrarCitaServicio(cita);

        HttpStatus status = "Cita registrada exitosamente".equals(response.getMessage())
                ? HttpStatus.CREATED
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }

    @PutMapping("/appointment/update/{id}")
    public ResponseEntity<ApiResponse<CitaResponse>> actualizarCitaController(@PathVariable Integer id,
            @RequestBody Cita citaActualizada) {
        ApiResponse<CitaResponse> response = citaServicio.actualizarCitaServicio(id, citaActualizada);

        HttpStatus status = "Cita actualizada exitosamente".equals(response.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }

    @GetMapping("/appointment/{idUsuario}")
    public ResponseEntity<List<CitaResponse>> obtenerCitasDeUsuarioController(@PathVariable Integer idUsuario) {
        List<CitaResponse> citasResponse = citaServicio.obtenerCitasDeUsuarioServicio(idUsuario);
        return new ResponseEntity<>(citasResponse, HttpStatus.OK);
    }

    @DeleteMapping("/appointment/delete/{id}")
    public ResponseEntity<ApiResponse<Void>> eliminarCitaController(@PathVariable Integer id) {
        ApiResponse<Void> response = citaServicio.eliminarCitaServicio(id);

        HttpStatus status = "Cita eliminada exitosamente".equals(response.getMessage())
                ? HttpStatus.OK
                : HttpStatus.BAD_REQUEST;

        return new ResponseEntity<>(response, status);
    }
}
