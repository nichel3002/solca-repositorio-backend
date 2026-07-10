package ec.edu.solca.repositorio.repository;

import ec.edu.solca.repositorio.model.UsuarioClinico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsuarioClinicoRepository extends JpaRepository<UsuarioClinico, String> {
}
