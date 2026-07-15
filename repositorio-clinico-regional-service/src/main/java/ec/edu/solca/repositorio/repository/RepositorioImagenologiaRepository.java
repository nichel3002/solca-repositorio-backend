package ec.edu.solca.repositorio.repository;

import ec.edu.solca.repositorio.model.RepositorioImagenologia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioImagenologiaRepository extends JpaRepository<RepositorioImagenologia, Long> {
    List<RepositorioImagenologia> findByIdPacienteRegionalOrderByFechaEstudioDesc(String idPacienteRegional);
    void deleteByIdPacienteRegional(String idPacienteRegional);
}
