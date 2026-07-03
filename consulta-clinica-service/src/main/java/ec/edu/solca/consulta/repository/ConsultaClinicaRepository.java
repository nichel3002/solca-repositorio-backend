package ec.edu.solca.consulta.repository;

import ec.edu.solca.consulta.model.ConsultaClinica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ConsultaClinicaRepository extends JpaRepository<ConsultaClinica, Long> {
    List<ConsultaClinica> findByIdPacienteRegional(String idPacienteRegional);
}
