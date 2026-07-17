package ec.edu.solca.repositorio.repository;

import ec.edu.solca.repositorio.model.RegistroClinicoRegional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RegistroClinicoRegionalRepository extends JpaRepository<RegistroClinicoRegional, Long> {
    List<RegistroClinicoRegional> findByIdPacienteRegionalOrderByModuloAscFechaRegistroDesc(String idPacienteRegional);
    List<RegistroClinicoRegional> findByIdPacienteRegionalOrderByActualizadoEnDescModuloAscFechaRegistroDesc(String idPacienteRegional);
    void deleteByIdPacienteRegional(String idPacienteRegional);
}
