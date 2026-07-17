package ec.edu.solca.historia.controller;

import ec.edu.solca.historia.model.Cie10;
import ec.edu.solca.historia.model.HistoriaClinica;
import ec.edu.solca.historia.repository.Cie10Repository;
import ec.edu.solca.historia.repository.HistoriaClinicaRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/historias")
public class HistoriaClinicaController {
    private final HistoriaClinicaRepository historiaRepository;
    private final Cie10Repository cie10Repository;

    public HistoriaClinicaController(HistoriaClinicaRepository historiaRepository, Cie10Repository cie10Repository) {
        this.historiaRepository = historiaRepository;
        this.cie10Repository = cie10Repository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public List<HistoriaClinica> listar() {
        return historiaRepository.findAll();
    }

    @GetMapping("/{idHistoriaClinica}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<HistoriaClinica> obtener(@PathVariable String idHistoriaClinica) {
        return historiaRepository.findById(idHistoriaClinica)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paciente/{idPacienteRegional}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public List<HistoriaClinica> listarPorPaciente(@PathVariable String idPacienteRegional) {
        return historiaRepository.findByIdPacienteRegionalOrderByFechaCreacionDescHoraCreacionDesc(idPacienteRegional);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<?> crear(@Valid @RequestBody HistoriaClinica historia, Authentication authentication) {
        String error = validarHistoria(historia);
        if (!error.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("error", error));
        }
        historia.setIdHistoriaClinica(null);
        historia.setCreadoPor(authentication.getName());
        historia.setRolCreador(rol(authentication));
        historia.setActualizadoPor(authentication.getName());
        HistoriaClinica guardada = historiaRepository.save(historia);
        return ResponseEntity.created(URI.create("/historias/" + guardada.getIdHistoriaClinica())).body(guardada);
    }

    @GetMapping("/cie10")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public List<Cie10> buscarCie10(@RequestParam(defaultValue = "") String q) {
        String termino = q.trim();
        if (termino.isBlank()) {
            return cie10Repository.findAll().stream().limit(25).toList();
        }
        return cie10Repository.findTop25ByCodigoContainingIgnoreCaseOrDescripcionContainingIgnoreCaseOrSintomasContainingIgnoreCaseOrCodigoOmsContainingIgnoreCaseOrderByCodigoAsc(
                termino,
                termino,
                termino,
                termino
        );
    }

    private String validarHistoria(HistoriaClinica historia) {
        if (historia.getIdPacienteRegional() == null || historia.getIdPacienteRegional().isBlank()) {
            return "El paciente regional es obligatorio.";
        }
        if (historia.getCodigoCie10() == null || historia.getCodigoCie10().isBlank()) {
            return "Seleccione un diagnostico CIE10.";
        }
        if (!cie10Repository.existsById(historia.getCodigoCie10())) {
            return "El codigo CIE10 no existe en la base de datos.";
        }
        if (historia.getMotivoConsulta() == null || historia.getMotivoConsulta().isBlank()) {
            return "El motivo de consulta es obligatorio.";
        }
        if (historia.getEnfermedadActual() == null || historia.getEnfermedadActual().isBlank()) {
            return "La enfermedad actual es obligatoria.";
        }
        return "";
    }

    private String rol(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .map(valor -> valor.replaceFirst("^ROLE_", ""))
                .orElse("SIN_ROL");
    }
}
