package ec.edu.solca.repositorio.controller;

import ec.edu.solca.repositorio.dto.HistoriaClinicaRegionalResponse;
import ec.edu.solca.repositorio.model.RegistroRepositorio;
import ec.edu.solca.repositorio.repository.RegistroRepositorioRepository;
import ec.edu.solca.repositorio.service.RepositorioIntegracionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public HistoriaClinicaRegionalResponse obtenerHistoria(@PathVariable String idPacienteRegional, Authentication authentication) {
        HistoriaClinicaRegionalResponse response = integracionService.consolidar(idPacienteRegional);
        auditar(idPacienteRegional, "ID_REGIONAL", "/repositorio/paciente/" + idPacienteRegional, response, authentication);
        return response;
    }

    @GetMapping("/cedula/{cedula}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public HistoriaClinicaRegionalResponse obtenerHistoriaPorCedula(@PathVariable String cedula, Authentication authentication) {
        HistoriaClinicaRegionalResponse response = integracionService.consolidarPorCedula(cedula);
        auditar(extraerIdPacienteRegional(response), "CEDULA:" + cedula, "/repositorio/cedula/" + cedula, response, authentication);
        return response;
    }

    @GetMapping("/registros")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RegistroRepositorio> listarRegistros() {
        return registroRepository.findAll();
    }

    @GetMapping("/auditoria")
    @PreAuthorize("hasRole('ADMIN')")
    public List<RegistroRepositorio> listarAuditoria() {
        return registroRepository.findAll();
    }

    @PostMapping("/pacientes")
    @PreAuthorize("hasRole('ADMIN')")
    public Object crearPaciente(@RequestBody Object paciente) {
        return integracionService.crearPaciente(paciente);
    }

    @PostMapping("/consultas")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public Object crearConsulta(@RequestBody Object consulta) {
        return integracionService.crearConsulta(consulta);
    }

    @PostMapping("/laboratorio")
    @PreAuthorize("hasAnyRole('ADMIN', 'LABORATORIO')")
    public Object crearLaboratorio(@RequestBody Object resultado) {
        return integracionService.crearLaboratorio(resultado);
    }

    @PostMapping("/imagenes")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public Object crearImagen(@RequestBody Object estudio) {
        return integracionService.crearImagen(estudio);
    }

    private void auditar(String idPacienteRegional, String criterioBusqueda, String endpoint,
                         HistoriaClinicaRegionalResponse response, Authentication authentication) {
        RegistroRepositorio registro = new RegistroRepositorio();
        registro.setIdPacienteRegional(idPacienteRegional);
        registro.setCriterioBusqueda(criterioBusqueda);
        registro.setEndpoint(endpoint);
        registro.setUsuario(authentication.getName());
        registro.setRol(authentication.getAuthorities().stream().findFirst().map(Object::toString).orElse("SIN_ROL"));
        registro.setFechaConsultaRepositorio(LocalDateTime.now().toString());
        registro.setResultado(response.getErrores().isEmpty() ? "CONSOLIDADO_OK" : "CONSOLIDADO_CON_ERRORES");
        registroRepository.save(registro);
    }

    private String extraerIdPacienteRegional(HistoriaClinicaRegionalResponse response) {
        if (response.getPaciente() instanceof java.util.Map<?, ?> datos && datos.get("idPacienteRegional") != null) {
            return String.valueOf(datos.get("idPacienteRegional"));
        }
        return "";
    }
}
