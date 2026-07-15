package ec.edu.solca.repositorio.controller;

import ec.edu.solca.repositorio.dto.HistoriaClinicaRegionalResponse;
import ec.edu.solca.repositorio.model.RegistroClinicoRegional;
import ec.edu.solca.repositorio.model.RegistroRepositorio;
import ec.edu.solca.repositorio.repository.RegistroRepositorioRepository;
import ec.edu.solca.repositorio.service.RepositorioIntegracionService;
import jakarta.servlet.http.HttpServletRequest;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    public HistoriaClinicaRegionalResponse obtenerHistoria(@PathVariable String idPacienteRegional, Authentication authentication, HttpServletRequest request) {
        HistoriaClinicaRegionalResponse response = integracionService.consolidar(idPacienteRegional);
        auditar(idPacienteRegional, "ID_REGIONAL", "/repositorio/paciente/" + idPacienteRegional,
                "Repositorio regional, Paciente maestro, Consulta clinica, Laboratorio clinico, Imagenologia",
                "Consultar historia por Master ID", resultadoConsolidacion(response), authentication, request);
        return response;
    }

    @GetMapping("/cedula/{cedula}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public HistoriaClinicaRegionalResponse obtenerHistoriaPorCedula(@PathVariable String cedula, Authentication authentication, HttpServletRequest request) {
        HistoriaClinicaRegionalResponse response = integracionService.consolidarPorCedula(cedula);
        auditar(extraerIdPacienteRegional(response), "CEDULA:" + cedula, "/repositorio/cedula/" + cedula,
                "Repositorio regional, Paciente maestro, Consulta clinica, Laboratorio clinico, Imagenologia",
                "Consultar historia por cedula", resultadoConsolidacion(response), authentication, request);
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

    @GetMapping("/clinico")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public List<RegistroClinicoRegional> listarRepositorioClinico() {
        return integracionService.listarRepositorioClinico();
    }

    @GetMapping("/clinico/paciente/{idPacienteRegional}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public List<RegistroClinicoRegional> listarRepositorioClinicoPorPaciente(@PathVariable String idPacienteRegional) {
        return integracionService.listarRepositorioClinicoPorPaciente(idPacienteRegional);
    }

    @GetMapping("/servicios")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO', 'LABORATORIO')")
    public List<java.util.Map<String, Object>> listarServicios() {
        return integracionService.consultarServiciosDisponibles();
    }

    @PostMapping("/pacientes")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public Object crearPaciente(@RequestBody Object paciente, Authentication authentication, HttpServletRequest request) {
        String errorValidacion = validarPaciente(paciente);
        if (!errorValidacion.isBlank()) {
            auditar(pacienteDesdeObjeto(paciente), "REGISTRO_PACIENTE", "/repositorio/pacientes",
                    "Paciente maestro", "Registrar paciente", "VALIDACION_ERROR: " + errorValidacion, authentication, request);
            return org.springframework.http.ResponseEntity.badRequest().body(Map.of("error", errorValidacion));
        }
        Object creado = integracionService.crearPaciente(paciente);
        auditar(pacienteDesdeObjeto(creado), "REGISTRO_PACIENTE", "/repositorio/pacientes",
                "Paciente maestro", "Registrar paciente", "PACIENTE_REGISTRADO", authentication, request);
        return creado;
    }

    @PostMapping("/consultas")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public Object crearConsulta(@RequestBody Object consulta, Authentication authentication, HttpServletRequest request) {
        Object creado = integracionService.crearConsulta(consulta);
        auditar(pacienteDesdeObjeto(consulta), "REGISTRO_CONSULTA", "/repositorio/consultas",
                "Consulta clinica", "Registrar consulta", "CONSULTA_REGISTRADA", authentication, request);
        return creado;
    }

    @PostMapping("/laboratorio")
    @PreAuthorize("hasAnyRole('ADMIN', 'LABORATORIO')")
    public Object crearLaboratorio(@RequestBody Object resultado, Authentication authentication, HttpServletRequest request) {
        Object creado = integracionService.crearLaboratorio(resultado);
        auditar(pacienteDesdeObjeto(resultado), "REGISTRO_LABORATORIO", "/repositorio/laboratorio",
                "Laboratorio clinico", "Registrar laboratorio", "RESULTADO_LABORATORIO_REGISTRADO", authentication, request);
        return creado;
    }

    @PostMapping("/imagenes")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public Object crearImagen(@RequestBody Object estudio, Authentication authentication, HttpServletRequest request) {
        Object creado = integracionService.crearImagen(estudio);
        auditar(pacienteDesdeObjeto(estudio), "REGISTRO_IMAGENOLOGIA", "/repositorio/imagenes",
                "Imagenologia", "Registrar estudio de imagenologia", "ESTUDIO_IMAGENOLOGIA_REGISTRADO", authentication, request);
        return creado;
    }

    @GetMapping("/imagenes/{id}/dicom")
    @PreAuthorize("hasAnyRole('ADMIN', 'MEDICO')")
    public ResponseEntity<byte[]> descargarDicom(@PathVariable Long id, @RequestHeader("Authorization") String authorization,
                                                 Authentication authentication, HttpServletRequest request) {
        ResponseEntity<byte[]> response = integracionService.descargarDicom(id, authorization);
        auditar("DICOM:" + id, "VISOR_DICOM", "/repositorio/imagenes/" + id + "/dicom",
                "Imagenologia", "Visualizar DICOM", "DICOM_DESCARGADO_HTTP_" + response.getStatusCode().value(), authentication, request);
        return response;
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
            @RequestHeader("Authorization") String authorization,
            Authentication authentication,
            HttpServletRequest request) throws IOException {
        String errorValidacion = validarDicom(idPacienteRegional, fechaEstudio, modalidad, descripcion, archivo);
        if (!errorValidacion.isBlank()) {
            auditar(idPacienteRegional, "ENVIO_DICOM", "/repositorio/imagenes/dicom",
                    "Imagenologia", "Enviar DICOM", "VALIDACION_ERROR: " + errorValidacion, authentication, request);
            return org.springframework.http.ResponseEntity.badRequest().body(Map.of("error", errorValidacion));
        }
        Object enviado = integracionService.enviarDicom(idPacienteRegional, sede, fechaEstudio, modalidad, descripcion, archivo, authorization);
        auditar(idPacienteRegional, "ENVIO_DICOM", "/repositorio/imagenes/dicom",
                "Imagenologia", "Enviar DICOM", "DICOM_ENVIADO_A_IMAGENOLOGIA", authentication, request);
        return enviado;
    }

    private void auditar(String idPacienteRegional, String criterioBusqueda, String endpoint,
                         String modulos, String accion, String resultados, Authentication authentication, HttpServletRequest request) {
        LocalDateTime ahora = LocalDateTime.now();
        RegistroRepositorio registro = new RegistroRepositorio();
        registro.setIdPacienteRegional(idPacienteRegional);
        registro.setPaciente(idPacienteRegional);
        registro.setCriterioBusqueda(criterioBusqueda);
        registro.setEndpoint(endpoint);
        registro.setUsuario(authentication.getName());
        registro.setRol(rol(authentication));
        registro.setFechaConsultaRepositorio(ahora.toString());
        registro.setFecha(LocalDate.now().toString());
        registro.setHora(LocalTime.now().withNano(0).toString());
        registro.setDireccionIp(direccionIp(request));
        registro.setModulos(modulos);
        registro.setAccion(accion);
        registro.setResultado(resultados);
        registro.setResultados(resultados);
        registroRepository.save(registro);
    }

    private String resultadoConsolidacion(HistoriaClinicaRegionalResponse response) {
        return response.getErrores().isEmpty() ? "CONSOLIDADO_OK" : "CONSOLIDADO_CON_ERRORES";
    }

    private String rol(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .findFirst()
                .map(Object::toString)
                .map(valor -> valor.replaceFirst("^ROLE_", ""))
                .orElse("SIN_ROL");
    }

    private String direccionIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        String realIp = request.getHeader("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp.trim();
        }
        return request.getRemoteAddr();
    }

    private String extraerIdPacienteRegional(HistoriaClinicaRegionalResponse response) {
        if (response.getPaciente() instanceof java.util.Map<?, ?> datos && datos.get("idPacienteRegional") != null) {
            return String.valueOf(datos.get("idPacienteRegional"));
        }
        return "";
    }

    private String pacienteDesdeObjeto(Object valor) {
        if (valor instanceof Map<?, ?> datos && datos.get("idPacienteRegional") != null) {
            return String.valueOf(datos.get("idPacienteRegional"));
        }
        return "No registrado";
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
