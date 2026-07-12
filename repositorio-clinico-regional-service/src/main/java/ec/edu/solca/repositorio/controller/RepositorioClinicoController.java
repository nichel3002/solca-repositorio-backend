package ec.edu.solca.repositorio.controller;

import ec.edu.solca.repositorio.dto.HistoriaClinicaRegionalResponse;
import ec.edu.solca.repositorio.model.RegistroRepositorio;
import ec.edu.solca.repositorio.repository.RegistroRepositorioRepository;
import ec.edu.solca.repositorio.service.RepositorioIntegracionService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    @GetMapping("/servicios")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'LABORATORIO')")
    public List<java.util.Map<String, Object>> listarServicios() {
        return integracionService.consultarServiciosDisponibles();
    }

    @PostMapping("/pacientes")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public Object crearPaciente(@RequestBody Object paciente) {
        String errorValidacion = validarPaciente(paciente);
        if (!errorValidacion.isBlank()) {
            return org.springframework.http.ResponseEntity.badRequest().body(Map.of("error", errorValidacion));
        }
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

    @GetMapping("/imagenes/{id}/dicom")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<byte[]> descargarDicom(@PathVariable Long id, @RequestHeader("Authorization") String authorization) {
        return integracionService.descargarDicom(id, authorization);
    }

    @PostMapping(value = "/imagenes/dicom", consumes = "multipart/form-data")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public Object enviarDicom(
            @RequestParam String idPacienteRegional,
            @RequestParam String sede,
            @RequestParam String fechaEstudio,
            @RequestParam String modalidad,
            @RequestParam String descripcion,
            @RequestParam MultipartFile archivo,
            @RequestHeader("Authorization") String authorization) throws IOException {
        String errorValidacion = validarDicom(idPacienteRegional, fechaEstudio, modalidad, descripcion, archivo);
        if (!errorValidacion.isBlank()) {
            return org.springframework.http.ResponseEntity.badRequest().body(Map.of("error", errorValidacion));
        }
        return integracionService.enviarDicom(idPacienteRegional, sede, fechaEstudio, modalidad, descripcion, archivo, authorization);
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

    private String validarPaciente(Object paciente) {
        if (!(paciente instanceof Map<?, ?> datos)) {
            return "El paciente debe enviarse como objeto JSON.";
        }
        String cedula = texto(datos.get("cedula"));
        String nombres = texto(datos.get("nombres"));
        String apellidos = texto(datos.get("apellidos"));
        String telefono = texto(datos.get("telefono"));
        if (!cedula.matches("^[0-9]{10}$")) {
            return "La cedula debe tener 10 digitos numericos.";
        }
        if (!nombres.matches("^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]{2,}$")) {
            return "Los nombres solo deben contener letras y espacios.";
        }
        if (!apellidos.matches("^[A-Za-zÁÉÍÓÚÜÑáéíóúüñ ]{2,}$")) {
            return "Los apellidos solo deben contener letras y espacios.";
        }
        if (!telefono.isBlank() && !telefono.matches("^[0-9]{7,10}$")) {
            return "El telefono debe tener entre 7 y 10 digitos.";
        }
        return "";
    }

    private String validarDicom(String idPacienteRegional, String fechaEstudio, String modalidad, String descripcion, MultipartFile archivo) {
        if (texto(idPacienteRegional).isBlank()) {
            return "Primero cargue un paciente.";
        }
        if (texto(fechaEstudio).isBlank()) {
            return "La fecha del estudio es obligatoria.";
        }
        if (texto(modalidad).isBlank()) {
            return "La modalidad de imagenologia es obligatoria.";
        }
        if (texto(descripcion).isBlank()) {
            return "La descripcion del estudio es obligatoria.";
        }
        if (archivo == null || archivo.isEmpty()) {
            return "Adjunte un archivo DICOM.";
        }
        String nombre = archivo.getOriginalFilename() == null ? "" : archivo.getOriginalFilename().toLowerCase();
        if (!nombre.endsWith(".dcm") && !nombre.endsWith(".dicom")) {
            return "Solo se permite subir archivos DICOM (.dcm o .dicom).";
        }
        return "";
    }

    private String texto(Object valor) {
        return valor == null ? "" : String.valueOf(valor).trim();
    }
}
