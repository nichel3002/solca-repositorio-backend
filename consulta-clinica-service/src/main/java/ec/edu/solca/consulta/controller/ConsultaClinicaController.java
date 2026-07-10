package ec.edu.solca.consulta.controller;

import ec.edu.solca.consulta.model.ConsultaClinica;
import ec.edu.solca.consulta.repository.ConsultaClinicaRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/consultas")
public class ConsultaClinicaController {
    private final ConsultaClinicaRepository repository;

    public ConsultaClinicaController(ConsultaClinicaRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public List<ConsultaClinica> listar() {
        return repository.findAll();
    }

    @GetMapping("/paciente/{idPacienteRegional}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public List<ConsultaClinica> listarPorPaciente(@PathVariable String idPacienteRegional) {
        return repository.findByIdPacienteRegional(idPacienteRegional);
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<ConsultaClinica> crear(@Valid @RequestBody ConsultaClinica consulta) {
        ConsultaClinica guardada = repository.save(consulta);
        return ResponseEntity.created(URI.create("/consultas/" + guardada.getId())).body(guardada);
    }
}
