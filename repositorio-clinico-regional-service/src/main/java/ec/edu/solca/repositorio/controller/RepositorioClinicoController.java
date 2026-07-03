package ec.edu.solca.repositorio.controller;

import ec.edu.solca.repositorio.dto.HistoriaClinicaRegionalResponse;
import ec.edu.solca.repositorio.model.RegistroRepositorio;
import ec.edu.solca.repositorio.repository.RegistroRepositorioRepository;
import ec.edu.solca.repositorio.service.RepositorioIntegracionService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/repositorio")
public class RepositorioClinicoController {
    private final RepositorioIntegracionService integracionService;
    private final RegistroRepositorioRepository registroRepository;

    public RepositorioClinicoController(RepositorioIntegracionService integracionService, RegistroRepositorioRepository registroRepository) {
        this.integracionService = integracionService;
        this.registroRepository = registroRepository;
    }

    @GetMapping("/paciente/{idPacienteRegional}")
    public HistoriaClinicaRegionalResponse obtenerHistoria(@PathVariable String idPacienteRegional) {
        HistoriaClinicaRegionalResponse response = integracionService.consolidar(idPacienteRegional);
        RegistroRepositorio registro = new RegistroRepositorio();
        registro.setIdPacienteRegional(idPacienteRegional);
        registro.setFechaConsultaRepositorio(LocalDateTime.now().toString());
        registro.setResultado(response.getErrores().isEmpty() ? "CONSOLIDADO_OK" : "CONSOLIDADO_CON_ERRORES");
        registroRepository.save(registro);
        return response;
    }

    @GetMapping("/registros")
    public List<RegistroRepositorio> listarRegistros() {
        return registroRepository.findAll();
    }
}
