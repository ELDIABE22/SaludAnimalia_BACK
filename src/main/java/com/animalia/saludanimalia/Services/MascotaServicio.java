package com.animalia.saludanimalia.Services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.animalia.saludanimalia.Models.Mascota;
import com.animalia.saludanimalia.Models.Usuario;
import com.animalia.saludanimalia.Models.response.ApiResponse;
import com.animalia.saludanimalia.Models.response.MascotaResponse;
import com.animalia.saludanimalia.Repository.CitaRepositorio;
import com.animalia.saludanimalia.Repository.MascotaRepositorio;
import com.animalia.saludanimalia.Repository.UsuarioRepositorio;

import jakarta.transaction.Transactional;

@Service
public class MascotaServicio {
    @Autowired
    MascotaRepositorio mascotaRepositorio;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    @Autowired
    private CitaRepositorio citaRepositorio;

    public ApiResponse<MascotaResponse> agregarMascotaServicio(Mascota mascota) {
        Usuario usuario = usuarioRepositorio.findById(mascota.getIdUsuario()).orElse(null);

        if (usuario == null) {
            return ApiResponse.<MascotaResponse>builder()
                    .data(null)
                    .message("Usuario no encontrado")
                    .build();
        }

        Mascota nuevaMascota = mascotaRepositorio.save(mascota);

        MascotaResponse mascotaResponse = MascotaResponse.builder()
                .id(nuevaMascota.getId())
                .usuario(usuario)
                .nombre(nuevaMascota.getNombre())
                .especie(nuevaMascota.getEspecie())
                .raza(nuevaMascota.getRaza())
                .edad(nuevaMascota.getEdad())
                .build();

        return ApiResponse.<MascotaResponse>builder()
                .data(mascotaResponse)
                .message("Mascota agregada exitosamente")
                .build();
    }

    public ApiResponse<MascotaResponse> actualizarMascotaServicio(Mascota mascotaActualizada) {
        Mascota mascotaExistente = mascotaRepositorio.findById(mascotaActualizada.getId()).orElse(null);

        if (mascotaExistente == null) {
            return ApiResponse.<MascotaResponse>builder().data(null).message("Mascota no encontrada").build();
        }

        Usuario usuario = usuarioRepositorio.findById(mascotaActualizada.getIdUsuario()).orElse(null);

        if (usuario == null) {
            return ApiResponse.<MascotaResponse>builder().data(null).message("Usuario no encontrado").build();
        }

        mascotaExistente.setNombre(mascotaActualizada.getNombre());
        mascotaExistente.setEspecie(mascotaActualizada.getEspecie());
        mascotaExistente.setRaza(mascotaActualizada.getRaza());
        mascotaExistente.setEdad(mascotaActualizada.getEdad());
        mascotaExistente.setIdUsuario(mascotaActualizada.getIdUsuario());

        Mascota mascotaActualizadaDb = mascotaRepositorio.save(mascotaExistente);
        MascotaResponse mascotaResponse = MascotaResponse.builder()
                .id(mascotaActualizadaDb.getId())
                .usuario(usuario)
                .nombre(mascotaActualizadaDb.getNombre())
                .especie(mascotaActualizadaDb.getEspecie())
                .raza(mascotaActualizadaDb.getRaza())
                .edad(mascotaActualizadaDb.getEdad())
                .build();

        return ApiResponse.<MascotaResponse>builder()
                .data(mascotaResponse)
                .message("Mascota actualizada exitosamente")
                .build();
    }

    public List<Mascota> obtenerMascotasDeUsuario(Integer idUsuario) {
        return mascotaRepositorio.findByIdUsuario(idUsuario);
    }

    @Transactional
    public ApiResponse<Void> eliminarMascota(Integer id) {
        if (!mascotaRepositorio.existsById(id)) {
            return ApiResponse.<Void>builder()
                    .data(null)
                    .message("Mascota no encontrada")
                    .build();
        }

        citaRepositorio.deleteByIdMascota(id);

        mascotaRepositorio.deleteById(id);
        return ApiResponse.<Void>builder()
                .data(null)
                .message("Mascota eliminada exitosamente")
                .build();
    }
}
