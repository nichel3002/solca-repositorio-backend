package ec.edu.solca.imagenologia.controller;

import ec.edu.solca.imagenologia.model.EstudioImagen;
import ec.edu.solca.imagenologia.repository.EstudioImagenRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/imagenes")
public class ImagenologiaController {
    private final EstudioImagenRepository repository;

    public ImagenologiaController(EstudioImagenRepository repository) {
        this.repository = repository;
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public List<EstudioImagen> listar() {
        return repository.findAll();
    }

    @GetMapping("/paciente/{idPacienteRegional}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public List<EstudioImagen> listarPorPaciente(@PathVariable String idPacienteRegional) {
        return repository.findByIdPacienteRegional(idPacienteRegional);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EstudioImagen> crear(@Valid @RequestBody EstudioImagen estudio) {
        EstudioImagen guardado = repository.save(estudio);
        return ResponseEntity.created(URI.create("/imagenes/" + guardado.getId())).body(guardado);
    }

    @PostMapping(value = "/dicom", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<?> recibirDicom(
            @RequestParam String idPacienteRegional,
            @RequestParam String sede,
            @RequestParam String fechaEstudio,
            @RequestParam String modalidad,
            @RequestParam String descripcion,
            @RequestParam MultipartFile archivo) throws IOException {
        String nombre = archivo.getOriginalFilename() == null ? "" : archivo.getOriginalFilename().toLowerCase();
        if (archivo.isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Adjunte un archivo DICOM."));
        }
        if (!nombre.endsWith(".dcm") && !nombre.endsWith(".dicom")) {
            return ResponseEntity.badRequest().body(Map.of("error", "Solo se permite subir archivos DICOM (.dcm o .dicom)."));
        }
        byte[] contenido = archivo.getBytes();
        if (!esDicom(contenido)) {
            return ResponseEntity.badRequest().body(Map.of("error", "El archivo no contiene cabecera DICOM valida."));
        }

        EstudioImagen estudio = new EstudioImagen();
        estudio.setIdPacienteRegional(idPacienteRegional);
        estudio.setSede(sede);
        estudio.setFechaEstudio(fechaEstudio);
        estudio.setModalidad(modalidad);
        estudio.setDescripcion(descripcion);
        estudio.setArchivoDicom(archivo.getOriginalFilename());
        estudio.setProtocoloEnvio("DICOM C-STORE");
        estudio.setEstadoEnvio("ENVIADO_A_IMAGENOLOGIA");
        estudio.setUrlPacs("dicom://imagenologia-solca/" + idPacienteRegional + "/" + archivo.getOriginalFilename());
        estudio.setInformeRadiologico("Pendiente de informe radiologico en PACS.");

        EstudioImagen guardado = repository.save(estudio);
        return ResponseEntity.created(URI.create("/imagenes/" + guardado.getId())).body(guardado);
    }

    private boolean esDicom(byte[] contenido) {
        return contenido.length >= 132
                && contenido[128] == 'D'
                && contenido[129] == 'I'
                && contenido[130] == 'C'
                && contenido[131] == 'M';
    }
}
