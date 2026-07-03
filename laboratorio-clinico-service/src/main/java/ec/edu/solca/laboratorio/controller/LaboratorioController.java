package ec.edu.solca.laboratorio.controller;

import ec.edu.solca.laboratorio.model.ResultadoLaboratorio;
import ec.edu.solca.laboratorio.repository.ResultadoLaboratorioRepository;
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
@RequestMapping("/laboratorio")
public class LaboratorioController {
    private final ResultadoLaboratorioRepository repository;

    public LaboratorioController(ResultadoLaboratorioRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<ResultadoLaboratorio> listar() {
        return repository.findAll();
    }

    @GetMapping("/paciente/{idPacienteRegional}")
    public List<ResultadoLaboratorio> listarPorPaciente(@PathVariable String idPacienteRegional) {
        return repository.findByIdPacienteRegional(idPacienteRegional);
    }

    @PostMapping
    public ResponseEntity<ResultadoLaboratorio> crear(@Valid @RequestBody ResultadoLaboratorio resultado) {
        ResultadoLaboratorio guardado = repository.save(resultado);
        return ResponseEntity.created(URI.create("/laboratorio/" + guardado.getId())).body(guardado);
    }
}
