package ec.edu.solca.imagenologia.repository;

import ec.edu.solca.imagenologia.model.EstudioImagen;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstudioImagenRepository extends JpaRepository<EstudioImagen, Long> {
    List<EstudioImagen> findByIdPacienteRegional(String idPacienteRegional);
}
