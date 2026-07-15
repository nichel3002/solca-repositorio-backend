package ec.edu.solca.repositorio.repository;

import ec.edu.solca.repositorio.model.RepositorioConsulta;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RepositorioConsultaRepository extends JpaRepository<RepositorioConsulta, Long> {
    List<RepositorioConsulta> findByIdPacienteRegionalOrderByFechaConsultaDesc(String idPacienteRegional);
    void deleteByIdPacienteRegional(String idPacienteRegional);
}
