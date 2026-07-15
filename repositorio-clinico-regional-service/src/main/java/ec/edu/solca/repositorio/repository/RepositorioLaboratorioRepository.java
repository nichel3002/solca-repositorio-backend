package ec.edu.solca.repositorio.repository;

import ec.edu.solca.repositorio.model.RepositorioLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioLaboratorioRepository extends JpaRepository<RepositorioLaboratorio, Long> {
    List<RepositorioLaboratorio> findByIdPacienteRegionalOrderByFechaResultadoDesc(String idPacienteRegional);
    void deleteByIdPacienteRegional(String idPacienteRegional);
}
