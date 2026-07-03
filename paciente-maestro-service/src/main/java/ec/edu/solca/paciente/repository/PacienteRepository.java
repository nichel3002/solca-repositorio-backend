package ec.edu.solca.paciente.repository;

import ec.edu.solca.paciente.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PacienteRepository extends JpaRepository<Paciente, String> {
}
