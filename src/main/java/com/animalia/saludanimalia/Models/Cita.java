package com.animalia.saludanimalia.Models;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "Citas")
public class Cita {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "id_usuario")
    private Integer idUsuario;

    @Column(name = "id_mascota")
    private Integer idMascota;

    @Column(name = "fecha_cita")
    private java.sql.Date fechaCita;

    @Column(name = "hora_cita")
    private java.sql.Time horaCita;

    private String motivo;

    @Column(name = "estado", columnDefinition = "VARCHAR(50) DEFAULT 'Programado'")
    private String estado;
}
