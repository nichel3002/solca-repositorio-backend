package ec.edu.solca.laboratorio.repository;

import ec.edu.solca.laboratorio.model.ResultadoLaboratorio;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResultadoLaboratorioRepository extends JpaRepository<ResultadoLaboratorio, Long> {
    List<ResultadoLaboratorio> findByIdPacienteRegional(String idPacienteRegional);
}
