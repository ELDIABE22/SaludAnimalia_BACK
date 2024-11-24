package com.animalia.saludanimalia.Repository;

import org.springframework.stereotype.Repository;
import com.animalia.saludanimalia.Models.Mascota;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

@Repository
public interface MascotaRepositorio extends CrudRepository<Mascota, Integer> {
    List<Mascota> findByIdUsuario(Integer idUsuario);
}
