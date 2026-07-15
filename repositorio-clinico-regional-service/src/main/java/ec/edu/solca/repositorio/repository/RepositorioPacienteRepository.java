package ec.edu.solca.repositorio.repository;

import ec.edu.solca.repositorio.model.RepositorioPaciente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioPacienteRepository extends JpaRepository<RepositorioPaciente, String> {
}
