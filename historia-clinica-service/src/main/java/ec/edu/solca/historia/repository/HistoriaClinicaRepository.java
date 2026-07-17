package ec.edu.solca.historia.repository;

import ec.edu.solca.historia.model.HistoriaClinica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HistoriaClinicaRepository extends JpaRepository<HistoriaClinica, String> {
    List<HistoriaClinica> findByIdPacienteRegionalOrderByFechaCreacionDescHoraCreacionDesc(String idPacienteRegional);
}
