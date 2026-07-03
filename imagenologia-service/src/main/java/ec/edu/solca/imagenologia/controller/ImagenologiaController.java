package ec.edu.solca.imagenologia.controller;

import ec.edu.solca.imagenologia.model.EstudioImagen;
import ec.edu.solca.imagenologia.repository.EstudioImagenRepository;
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
@RequestMapping("/imagenes")
public class ImagenologiaController {
    private final EstudioImagenRepository repository;

    public ImagenologiaController(EstudioImagenRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    public List<EstudioImagen> listar() {
        return repository.findAll();
    }

    @GetMapping("/paciente/{idPacienteRegional}")
    public List<EstudioImagen> listarPorPaciente(@PathVariable String idPacienteRegional) {
        return repository.findByIdPacienteRegional(idPacienteRegional);
    }

    @PostMapping
    public ResponseEntity<EstudioImagen> crear(@Valid @RequestBody EstudioImagen estudio) {
        EstudioImagen guardado = repository.save(estudio);
        return ResponseEntity.created(URI.create("/imagenes/" + guardado.getId())).body(guardado);
    }
}
