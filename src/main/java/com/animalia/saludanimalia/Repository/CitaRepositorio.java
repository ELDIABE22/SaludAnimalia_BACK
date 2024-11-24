package com.animalia.saludanimalia.Repository;

import org.springframework.stereotype.Repository;
import com.animalia.saludanimalia.Models.Cita;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

@Repository
public interface CitaRepositorio extends CrudRepository<Cita, Integer> {
    List<Cita> findByFechaCitaAndHoraCita(java.sql.Date fechaCita, java.sql.Time horaCita);

    List<Cita> findByIdUsuario(Integer idUsuario);

    void deleteByIdMascota(Integer idMascota);
}
