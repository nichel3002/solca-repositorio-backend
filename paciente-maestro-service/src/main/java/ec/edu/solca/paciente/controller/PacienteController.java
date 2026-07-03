package ec.edu.solca.paciente.controller;

import ec.edu.solca.paciente.model.Paciente;
import ec.edu.solca.paciente.repository.PacienteRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
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
@RequestMapping("/pacientes")
public class PacienteController {
    private final PacienteRepository repository;

    public PacienteController(PacienteRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<Paciente> listar() {
        return repository.findAll();
    }

    @GetMapping("/{idPacienteRegional}")
    public ResponseEntity<Paciente> obtener(@PathVariable String idPacienteRegional) {
        return repository.findById(idPacienteRegional)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Paciente> crear(@Valid @RequestBody Paciente paciente) {
        Paciente guardado = repository.save(paciente);
        return ResponseEntity.created(URI.create("/pacientes/" + guardado.getIdPacienteRegional())).body(guardado);
    }
}
