package com.animalia.saludanimalia.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.animalia.saludanimalia.Models.Cita;
import com.animalia.saludanimalia.Models.Mascota;
import com.animalia.saludanimalia.Models.Usuario;
import com.animalia.saludanimalia.Models.response.ApiResponse;
import com.animalia.saludanimalia.Models.response.CitaResponse;
import com.animalia.saludanimalia.Repository.CitaRepositorio;
import com.animalia.saludanimalia.Repository.MascotaRepositorio;
import com.animalia.saludanimalia.Repository.UsuarioRepositorio;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CitaServicio {

        @Autowired
        private CitaRepositorio citaRepositorio;

        @Autowired
        private MascotaRepositorio mascotaRepositorio;

        @Autowired
        private UsuarioRepositorio usuarioRepositorio;

        @Autowired
        private EmailService emailService;

        public ApiResponse<CitaResponse> registrarCitaServicio(Cita cita) {
                // Validar que la hora de la cita sea a en punto o y media
                int minutes = cita.getHoraCita().toLocalTime().getMinute();
                if (minutes != 0 && minutes != 30) {
                        return ApiResponse.<CitaResponse>builder()
                                        .data(null)
                                        .message("La hora de la cita debe ser a las en punto o a las y media")
                                        .build();
                }

                // Verificar disponibilidad de la hora y el estado diferente a Programado
                List<Cita> citasExistentes = citaRepositorio.findByFechaCitaAndHoraCita(cita.getFechaCita(),
                                cita.getHoraCita());
                for (Cita c : citasExistentes) {
                        if (c.getEstado().equals("Programado")) {
                                return ApiResponse.<CitaResponse>builder().data(null)
                                                .message("La hora seleccionada no está disponible")
                                                .build();
                        }
                }

                // Verificar que el usuario y la mascota existan
                Usuario usuario = usuarioRepositorio.findById(cita.getIdUsuario()).orElse(null);
                Mascota mascota = mascotaRepositorio.findById(cita.getIdMascota()).orElse(null);

                if (usuario == null) {
                        return ApiResponse.<CitaResponse>builder()
                                        .data(null)
                                        .message("Usuario no encontrado")
                                        .build();
                }

                if (mascota == null) {
                        return ApiResponse.<CitaResponse>builder()
                                        .data(null)
                                        .message("Mascota no encontrada")
                                        .build();
                }

                // Validar que la mascota pertenezca al usuario
                if (!mascota.getIdUsuario().equals(usuario.getId())) {
                        return ApiResponse.<CitaResponse>builder()
                                        .data(null)
                                        .message("La mascota no pertenece al usuario")
                                        .build();
                }

                // Asignar el estado por defecto a 'Programado'
                cita.setEstado("Programado");

                // Guardar la nueva cita
                Cita nuevaCita = citaRepositorio.save(cita);

                // Crear la respuesta CitaResponse
                CitaResponse citaResponse = CitaResponse.builder()
                                .id(nuevaCita.getId())
                                .usuario(usuario)
                                .mascota(mascota)
                                .fechaCita(nuevaCita.getFechaCita())
                                .horaCita(nuevaCita.getHoraCita())
                                .motivo(nuevaCita.getMotivo())
                                .estado(nuevaCita.getEstado())
                                .build();

                // Construir y enviar el correo de confirmación
                String citaDetails = "Fecha: " + nuevaCita.getFechaCita() + ", Hora: " + nuevaCita.getHoraCita()
                                + ", Motivo: "
                                + nuevaCita.getMotivo();
                String emailContent = emailService.buildCitaCreacionEmailContent(usuario.getNombre(), citaDetails);
                emailService.sendSimpleMessage(usuario.getEmail(), "Confirmación de Creación de Cita", emailContent);

                return ApiResponse.<CitaResponse>builder()
                                .data(citaResponse)
                                .message("Cita registrada exitosamente")
                                .build();
        }

        public ApiResponse<CitaResponse> actualizarCitaServicio(Integer id, Cita citaActualizada) {
                // Verificar que la cita existe
                Cita citaExistente = citaRepositorio.findById(id).orElse(null);
                if (citaExistente == null) {
                        return ApiResponse.<CitaResponse>builder()
                                        .data(null)
                                        .message("Cita no encontrada")
                                        .build();
                }

                // Validar que la hora de la cita sea a en punto o y media
                int minutes = citaActualizada.getHoraCita().toLocalTime().getMinute();
                if (minutes != 0 && minutes != 30) {
                        return ApiResponse.<CitaResponse>builder()
                                        .data(null)
                                        .message("La hora de la cita debe ser a las en punto o a las y media")
                                        .build();
                }

                // Verificar disponibilidad de la hora (excluir la cita actual)
                List<Cita> citasExistentes = citaRepositorio.findByFechaCitaAndHoraCita(citaActualizada.getFechaCita(),
                                citaActualizada.getHoraCita());
                citasExistentes.removeIf(c -> c.getId().equals(id));

                if (!citasExistentes.isEmpty()
                                && citasExistentes.stream().anyMatch(c -> c.getEstado().equals("Programado"))) {
                        return ApiResponse.<CitaResponse>builder()
                                        .data(null)
                                        .message("La hora seleccionada no está disponible")
                                        .build();
                }

                // Verificar que el usuario y la mascota existan
                Usuario usuario = usuarioRepositorio.findById(citaActualizada.getIdUsuario()).orElse(null);
                Mascota mascota = mascotaRepositorio.findById(citaActualizada.getIdMascota()).orElse(null);

                if (usuario == null) {
                        return ApiResponse.<CitaResponse>builder()
                                        .data(null)
                                        .message("Usuario no encontrado")
                                        .build();
                }

                if (mascota == null) {
                        return ApiResponse.<CitaResponse>builder()
                                        .data(null)
                                        .message("Mascota no encontrada")
                                        .build();
                }

                // Validar que la mascota pertenezca al usuario
                if (!mascota.getIdUsuario().equals(usuario.getId())) {
                        return ApiResponse.<CitaResponse>builder()
                                        .data(null)
                                        .message("La mascota no pertenece al usuario")
                                        .build();
                }

                // Actualizar los datos de la cita
                citaExistente.setFechaCita(citaActualizada.getFechaCita());
                citaExistente.setHoraCita(citaActualizada.getHoraCita());
                citaExistente.setMotivo(citaActualizada.getMotivo());
                citaExistente.setIdUsuario(citaActualizada.getIdUsuario());
                citaExistente.setIdMascota(citaActualizada.getIdMascota());
                citaExistente.setEstado(citaActualizada.getEstado());

                Cita citaActualizadaDb = citaRepositorio.save(citaExistente);

                // Crear la respuesta CitaResponse
                CitaResponse citaResponse = CitaResponse.builder()
                                .id(citaActualizadaDb.getId())
                                .usuario(usuario)
                                .mascota(mascota)
                                .fechaCita(citaActualizadaDb.getFechaCita())
                                .horaCita(citaActualizadaDb.getHoraCita())
                                .motivo(citaActualizadaDb.getMotivo())
                                .estado(citaActualizadaDb.getEstado())
                                .build();

                // Construir y enviar el correo de modificación
                String citaDetails = "Fecha: " + citaActualizadaDb.getFechaCita() + ", Hora: "
                                + citaActualizadaDb.getHoraCita()
                                + ", Motivo: " + citaActualizadaDb.getMotivo();
                String emailContent = emailService.buildCitaModificacionEmailContent(usuario.getNombre(), citaDetails);
                emailService.sendSimpleMessage(usuario.getEmail(), "Confirmación de Modificación de Cita",
                                emailContent);

                return ApiResponse.<CitaResponse>builder()
                                .data(citaResponse)
                                .message("Cita actualizada exitosamente")
                                .build();
        }

        public List<CitaResponse> obtenerCitasDeUsuarioServicio(Integer idUsuario) {
                // Verificar que el usuario existe
                Usuario usuario = usuarioRepositorio.findById(idUsuario).orElse(null);
                if (usuario == null) {
                        return null;
                }

                // Obtener todas las citas del usuario
                List<Cita> citas = citaRepositorio.findByIdUsuario(idUsuario);

                // Convertir las citas a CitaResponse
                return citas.stream().map(cita -> CitaResponse.builder()
                                .id(cita.getId())
                                .usuario(usuario)
                                .mascota(mascotaRepositorio.findById(cita.getIdMascota()).orElse(null))
                                .fechaCita(cita.getFechaCita())
                                .horaCita(cita.getHoraCita())
                                .motivo(cita.getMotivo())
                                .estado(cita.getEstado())
                                .build()).collect(Collectors.toList());
        }

        public ApiResponse<Void> eliminarCitaServicio(Integer id) {
                // Verificar que la cita existe
                Cita cita = citaRepositorio.findById(id).orElse(null);
                if (cita == null) {
                        return ApiResponse.<Void>builder()
                                        .data(null)
                                        .message("Cita no encontrada")
                                        .build();
                }

                // Crear los detalles de la cita para el correo antes de eliminarla
                Usuario usuario = usuarioRepositorio.findById(cita.getIdUsuario()).orElse(null);
                if (usuario == null) {
                        return ApiResponse.<Void>builder()
                                        .data(null)
                                        .message("Usuario no encontrado")
                                        .build();
                }

                String citaDetails = "Fecha: " + cita.getFechaCita() + ", Hora: " + cita.getHoraCita() + ", Motivo: "
                                + cita.getMotivo();
                String emailContent = emailService.buildCitaCancelacionEmailContent(usuario.getNombre(), citaDetails);

                // Eliminar la cita
                citaRepositorio.deleteById(id);

                // Enviar el correo de cancelación
                emailService.sendSimpleMessage(usuario.getEmail(), "Confirmación de Cancelación de Cita", emailContent);

                return ApiResponse.<Void>builder()
                                .data(null)
                                .message("Cita eliminada exitosamente")
                                .build();
        }
}
