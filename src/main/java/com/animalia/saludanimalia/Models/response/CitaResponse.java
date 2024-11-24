package com.animalia.saludanimalia.Models.response;

import com.animalia.saludanimalia.Models.Mascota;
import com.animalia.saludanimalia.Models.Usuario;

import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CitaResponse {
    private Integer id;
    private Usuario usuario;
    private Mascota mascota;
    private java.sql.Date fechaCita;
    private java.sql.Time horaCita;
    private String motivo;
    private String estado;
}
