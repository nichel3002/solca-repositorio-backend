package ec.edu.solca.paciente.repository;

import ec.edu.solca.paciente.model.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PacienteRepository extends JpaRepository<Paciente, String> {
    Optional<Paciente> findByCedula(String cedula);
}
