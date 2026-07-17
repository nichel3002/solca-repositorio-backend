package ec.edu.solca.historia.repository;

import ec.edu.solca.historia.model.Cie10;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface Cie10Repository extends JpaRepository<Cie10, String> {
    List<Cie10> findTop25ByCodigoContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrderByCodigoAsc(String codigo, String descripcion);
}
