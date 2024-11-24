package com.animalia.saludanimalia.Models.response;

import com.animalia.saludanimalia.Models.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MascotaResponse {
    private Integer id;
    private Usuario usuario;
    private String nombre;
    private String especie;
    private String raza;
    private Integer edad;
}
