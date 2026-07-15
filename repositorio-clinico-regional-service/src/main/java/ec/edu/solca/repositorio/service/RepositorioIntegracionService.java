package ec.edu.solca.repositorio.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import ec.edu.solca.repositorio.dto.HistoriaClinicaRegionalResponse;
import ec.edu.solca.repositorio.model.RegistroClinicoRegional;
import ec.edu.solca.repositorio.model.RepositorioConsulta;
import ec.edu.solca.repositorio.model.RepositorioImagenologia;
import ec.edu.solca.repositorio.model.RepositorioLaboratorio;
import ec.edu.solca.repositorio.model.RepositorioPaciente;
import ec.edu.solca.repositorio.repository.RegistroClinicoRegionalRepository;
import ec.edu.solca.repositorio.repository.RepositorioConsultaRepository;
import ec.edu.solca.repositorio.repository.RepositorioImagenologiaRepository;
import ec.edu.solca.repositorio.repository.RepositorioLaboratorioRepository;
import ec.edu.solca.repositorio.repository.RepositorioPacienteRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RepositorioIntegracionService {
    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final RegistroClinicoRegionalRepository registroClinicoRepository;
    private final RepositorioPacienteRepository repositorioPacienteRepository;
    private final RepositorioConsultaRepository repositorioConsultaRepository;
    private final RepositorioLaboratorioRepository repositorioLaboratorioRepository;
    private final RepositorioImagenologiaRepository repositorioImagenologiaRepository;
    private final String pacienteUrl;
    private final String consultaUrl;
    private final String laboratorioUrl;
    private final String imagenologiaUrl;

    public RepositorioIntegracionService(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            RegistroClinicoRegionalRepository registroClinicoRepository,
            RepositorioPacienteRepository repositorioPacienteRepository,
            RepositorioConsultaRepository repositorioConsultaRepository,
            RepositorioLaboratorioRepository repositorioLaboratorioRepository,
            RepositorioImagenologiaRepository repositorioImagenologiaRepository,
            @Value("${servicios.paciente-url}") String pacienteUrl,
            @Value("${servicios.consulta-url}") String consultaUrl,
            @Value("${servicios.laboratorio-url}") String laboratorioUrl,
            @Value("${servicios.imagenologia-url}") String imagenologiaUrl) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.registroClinicoRepository = registroClinicoRepository;
        this.repositorioPacienteRepository = repositorioPacienteRepository;
        this.repositorioConsultaRepository = repositorioConsultaRepository;
        this.repositorioLaboratorioRepository = repositorioLaboratorioRepository;
        this.repositorioImagenologiaRepository = repositorioImagenologiaRepository;
        this.pacienteUrl = pacienteUrl;
        this.consultaUrl = consultaUrl;
        this.laboratorioUrl = laboratorioUrl;
        this.imagenologiaUrl = imagenologiaUrl;
    }

    @Transactional
    public HistoriaClinicaRegionalResponse consolidar(String idPacienteRegional) {
        Map<String, String> errores = new LinkedHashMap<>();
        Object paciente = obtenerObjeto(pacienteUrl + "/pacientes/" + idPacienteRegional, "paciente-maestro", errores);
        List<Object> consultas = obtenerLista(consultaUrl + "/consultas/paciente/" + idPacienteRegional, "consulta-clinica", errores);
        List<Object> laboratorio = obtenerLista(laboratorioUrl + "/laboratorio/paciente/" + idPacienteRegional, "laboratorio-clinico", errores);
        List<Object> imagenes = obtenerLista(imagenologiaUrl + "/imagenes/paciente/" + idPacienteRegional, "imagenologia", errores);
        HistoriaClinicaRegionalResponse response = new HistoriaClinicaRegionalResponse(paciente, consultas, laboratorio, imagenes, errores);
        guardarRepositorioClinico(idPacienteRegional, response);
        return response;
    }

    @Transactional
    public HistoriaClinicaRegionalResponse consolidarPorCedula(String cedula) {
        Map<String, String> errores = new LinkedHashMap<>();
        Object paciente = obtenerObjeto(pacienteUrl + "/pacientes/cedula/" + cedula, "paciente-maestro", errores);
        String idPacienteRegional = extraerIdPacienteRegional(paciente);
        if (idPacienteRegional.isBlank()) {
            errores.put("repositorio-regional", "No se encontro paciente con la cedula indicada.");
            return new HistoriaClinicaRegionalResponse(paciente, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), errores);
        }

        List<Object> consultas = obtenerLista(consultaUrl + "/consultas/paciente/" + idPacienteRegional, "consulta-clinica", errores);
        List<Object> laboratorio = obtenerLista(laboratorioUrl + "/laboratorio/paciente/" + idPacienteRegional, "laboratorio-clinico", errores);
        List<Object> imagenes = obtenerLista(imagenologiaUrl + "/imagenes/paciente/" + idPacienteRegional, "imagenologia", errores);
        HistoriaClinicaRegionalResponse response = new HistoriaClinicaRegionalResponse(paciente, consultas, laboratorio, imagenes, errores);
        guardarRepositorioClinico(idPacienteRegional, response);
        return response;
    }

    public List<RegistroClinicoRegional> listarRepositorioClinico() {
        return registroClinicoRepository.findAll();
    }

    public List<RegistroClinicoRegional> listarRepositorioClinicoPorPaciente(String idPacienteRegional) {
        return registroClinicoRepository.findByIdPacienteRegionalOrderByModuloAscFechaRegistroDesc(idPacienteRegional);
    }

    public Object crearPaciente(Object paciente) {
        return crearObjeto(pacienteUrl + "/pacientes", paciente);
    }

    public Object crearConsulta(Object consulta) {
        return crearObjeto(consultaUrl + "/consultas", consulta);
    }

    public Object crearLaboratorio(Object resultado) {
        return crearObjeto(laboratorioUrl + "/laboratorio", resultado);
    }

    public Object crearImagen(Object estudio) {
        return crearObjeto(imagenologiaUrl + "/imagenes", estudio);
    }

    public Object enviarDicom(String idPacienteRegional, String sede, String fechaEstudio, String modalidad,
                              String descripcion, MultipartFile archivo, String authorization) throws IOException {
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("idPacienteRegional", idPacienteRegional);
        body.add("sede", sede);
        body.add("fechaEstudio", fechaEstudio);
        body.add("modalidad", modalidad);
        body.add("descripcion", descripcion);
        body.add("archivo", new ByteArrayResource(archivo.getBytes()) {
            @Override
            public String getFilename() {
                return archivo.getOriginalFilename();
            }
        });

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        headers.set(HttpHeaders.AUTHORIZATION, authorization);

        ResponseEntity<Object> response = restTemplate.exchange(
                imagenologiaUrl + "/imagenes/dicom",
                HttpMethod.POST,
                new HttpEntity<>(body, headers),
                Object.class);
        return response.getBody();
    }

    public ResponseEntity<byte[]> descargarDicom(Long id, String authorization) {
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, authorization);
        return restTemplate.exchange(
                imagenologiaUrl + "/imagenes/" + id + "/dicom",
                HttpMethod.GET,
                new HttpEntity<>(headers),
                byte[].class);
    }

    public List<Map<String, Object>> consultarServiciosDisponibles() {
        List<Map<String, Object>> servicios = new ArrayList<>();
        servicios.add(verificarServicio("Paciente maestro", "paciente-maestro", pacienteUrl + "/pacientes"));
        servicios.add(verificarServicio("Consulta clinica", "consulta-clinica", consultaUrl + "/consultas"));
        servicios.add(verificarServicio("Laboratorio clinico", "laboratorio-clinico", laboratorioUrl + "/laboratorio"));
        servicios.add(verificarServicio("Imagenologia / PACS", "imagenologia", imagenologiaUrl + "/imagenes"));
        return servicios;
    }

    private Object obtenerObjeto(String url, String servicio, Map<String, String> errores) {
        try {
            return restTemplate.getForObject(url, Object.class);
        } catch (RestClientException ex) {
            errores.put(servicio, "No se pudo consultar el microservicio: " + ex.getClass().getSimpleName());
            return Map.of();
        }
    }

    private List<Object> obtenerLista(String url, String servicio, Map<String, String> errores) {
        try {
            return restTemplate.exchange(url, HttpMethod.GET, null, new ParameterizedTypeReference<List<Object>>() {}).getBody();
        } catch (RestClientException ex) {
            errores.put(servicio, "No se pudo consultar el microservicio: " + ex.getClass().getSimpleName());
            return new ArrayList<>();
        }
    }

    private Object crearObjeto(String url, Object body) {
        ResponseEntity<Object> response = restTemplate.exchange(url, HttpMethod.POST, new HttpEntity<>(body), Object.class);
        return response.getBody();
    }

    private Map<String, Object> verificarServicio(String nombre, String codigo, String url) {
        Map<String, Object> estado = new LinkedHashMap<>();
        estado.put("nombre", nombre);
        estado.put("codigo", codigo);
        estado.put("endpoint", url);
        try {
            long inicio = System.currentTimeMillis();
            restTemplate.exchange(url, HttpMethod.GET, null, Object.class);
            estado.put("disponible", true);
            estado.put("mensaje", "Servicio disponible");
            estado.put("latenciaMs", System.currentTimeMillis() - inicio);
        } catch (RestClientException ex) {
            estado.put("disponible", false);
            estado.put("mensaje", "No disponible: " + ex.getClass().getSimpleName());
            estado.put("latenciaMs", null);
        }
        return estado;
    }

    private String extraerIdPacienteRegional(Object paciente) {
        if (paciente instanceof Map<?, ?> datos && datos.get("idPacienteRegional") != null) {
            return String.valueOf(datos.get("idPacienteRegional"));
        }
        return "";
    }

    private void guardarRepositorioClinico(String idPacienteRegional, HistoriaClinicaRegionalResponse response) {
        if (idPacienteRegional == null || idPacienteRegional.isBlank()) {
            return;
        }
        registroClinicoRepository.deleteByIdPacienteRegional(idPacienteRegional);
        repositorioConsultaRepository.deleteByIdPacienteRegional(idPacienteRegional);
        repositorioLaboratorioRepository.deleteByIdPacienteRegional(idPacienteRegional);
        repositorioImagenologiaRepository.deleteByIdPacienteRegional(idPacienteRegional);
        String actualizadoEn = LocalDateTime.now().toString();
        List<RegistroClinicoRegional> registros = new ArrayList<>();
        agregarRegistro(registros, idPacienteRegional, "PACIENTE_MAESTRO", "PACIENTE", response.getPaciente(), actualizadoEn);
        response.getConsultas().forEach(consulta ->
                agregarRegistro(registros, idPacienteRegional, "CONSULTA_CLINICA", "CONSULTA", consulta, actualizadoEn));
        response.getLaboratorio().forEach(resultado ->
                agregarRegistro(registros, idPacienteRegional, "LABORATORIO_CLINICO", "RESULTADO_LABORATORIO", resultado, actualizadoEn));
        response.getImagenes().forEach(imagen ->
                agregarRegistro(registros, idPacienteRegional, "IMAGENOLOGIA", "ESTUDIO_IMAGEN", imagen, actualizadoEn));
        registroClinicoRepository.saveAll(registros);
        guardarTablasExplicitas(idPacienteRegional, response, actualizadoEn);
    }

    private void guardarTablasExplicitas(String idPacienteRegional, HistoriaClinicaRegionalResponse response, String actualizadoEn) {
        if (response.getPaciente() instanceof Map<?, ?> paciente && !paciente.isEmpty()) {
            repositorioPacienteRepository.save(mapearPaciente(idPacienteRegional, paciente, actualizadoEn));
        }
        repositorioConsultaRepository.saveAll(response.getConsultas().stream()
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .map(consulta -> mapearConsulta(idPacienteRegional, consulta, actualizadoEn))
                .toList());
        repositorioLaboratorioRepository.saveAll(response.getLaboratorio().stream()
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .map(resultado -> mapearLaboratorio(idPacienteRegional, resultado, actualizadoEn))
                .toList());
        repositorioImagenologiaRepository.saveAll(response.getImagenes().stream()
                .filter(Map.class::isInstance)
                .map(Map.class::cast)
                .map(imagen -> mapearImagenologia(idPacienteRegional, imagen, actualizadoEn))
                .toList());
    }

    private RepositorioPaciente mapearPaciente(String idPacienteRegional, Map<?, ?> datos, String actualizadoEn) {
        RepositorioPaciente paciente = new RepositorioPaciente();
        paciente.setIdPacienteRegional(idPacienteRegional);
        paciente.setCedula(primero(datos, "cedula"));
        paciente.setNombres(primero(datos, "nombres"));
        paciente.setApellidos(primero(datos, "apellidos"));
        paciente.setSedeOrigen(primero(datos, "sedeOrigen"));
        paciente.setFechaNacimiento(primero(datos, "fechaNacimiento"));
        paciente.setSexo(primero(datos, "sexo"));
        paciente.setDireccion(primero(datos, "direccion"));
        paciente.setTelefono(primero(datos, "telefono"));
        paciente.setActualizadoEn(actualizadoEn);
        return paciente;
    }

    private RepositorioConsulta mapearConsulta(String idPacienteRegional, Map<?, ?> datos, String actualizadoEn) {
        RepositorioConsulta consulta = new RepositorioConsulta();
        consulta.setIdOrigen(numero(datos.get("id")));
        consulta.setIdPacienteRegional(idPacienteRegional);
        consulta.setSede(primero(datos, "sede"));
        consulta.setFechaConsulta(primero(datos, "fechaConsulta"));
        consulta.setEspecialidad(primero(datos, "especialidad"));
        consulta.setDiagnostico(primero(datos, "diagnostico"));
        consulta.setMedicoTratante(primero(datos, "medicoTratante"));
        consulta.setObservaciones(primero(datos, "observaciones"));
        consulta.setActualizadoEn(actualizadoEn);
        return consulta;
    }

    private RepositorioLaboratorio mapearLaboratorio(String idPacienteRegional, Map<?, ?> datos, String actualizadoEn) {
        RepositorioLaboratorio resultado = new RepositorioLaboratorio();
        resultado.setIdOrigen(numero(datos.get("id")));
        resultado.setIdPacienteRegional(idPacienteRegional);
        resultado.setSede(primero(datos, "sede"));
        resultado.setFechaResultado(primero(datos, "fechaResultado"));
        resultado.setTipoExamen(primero(datos, "tipoExamen"));
        resultado.setResultado(primero(datos, "resultado"));
        resultado.setUnidad(primero(datos, "unidad"));
        resultado.setRangoReferencia(primero(datos, "rangoReferencia"));
        resultado.setActualizadoEn(actualizadoEn);
        return resultado;
    }

    private RepositorioImagenologia mapearImagenologia(String idPacienteRegional, Map<?, ?> datos, String actualizadoEn) {
        RepositorioImagenologia imagen = new RepositorioImagenologia();
        imagen.setIdOrigen(numero(datos.get("id")));
        imagen.setIdPacienteRegional(idPacienteRegional);
        imagen.setSede(primero(datos, "sede"));
        imagen.setFechaEstudio(primero(datos, "fechaEstudio"));
        imagen.setModalidad(primero(datos, "modalidad"));
        imagen.setDescripcion(primero(datos, "descripcion"));
        imagen.setUrlPacs(primero(datos, "urlPacs"));
        imagen.setInformeRadiologico(primero(datos, "informeRadiologico"));
        imagen.setArchivoDicom(primero(datos, "archivoDicom"));
        imagen.setProtocoloEnvio(primero(datos, "protocoloEnvio"));
        imagen.setEstadoEnvio(primero(datos, "estadoEnvio"));
        imagen.setTieneDicom(!primero(datos, "archivoDicom").isBlank());
        imagen.setActualizadoEn(actualizadoEn);
        return imagen;
    }

    private void agregarRegistro(List<RegistroClinicoRegional> registros, String idPacienteRegional, String modulo,
                                 String tipoRegistro, Object datos, String actualizadoEn) {
        if (!(datos instanceof Map<?, ?> mapa) || mapa.isEmpty()) {
            return;
        }
        RegistroClinicoRegional registro = new RegistroClinicoRegional();
        registro.setIdPacienteRegional(idPacienteRegional);
        registro.setModulo(modulo);
        registro.setTipoRegistro(tipoRegistro);
        registro.setIdOrigen(texto(mapa.get("id")));
        registro.setSede(primero(mapa, "sede", "sedeOrigen"));
        registro.setFechaRegistro(primero(mapa, "fechaConsulta", "fechaResultado", "fechaEstudio", "fechaNacimiento"));
        registro.setResumen(resumen(tipoRegistro, mapa));
        registro.setDatosJson(json(datos));
        registro.setActualizadoEn(actualizadoEn);
        registros.add(registro);
    }

    private String resumen(String tipoRegistro, Map<?, ?> datos) {
        return switch (tipoRegistro) {
            case "PACIENTE" -> primero(datos, "nombres") + " " + primero(datos, "apellidos") + " - cedula " + primero(datos, "cedula");
            case "CONSULTA" -> primero(datos, "especialidad") + " - " + primero(datos, "diagnostico");
            case "RESULTADO_LABORATORIO" -> primero(datos, "tipoExamen") + " - " + primero(datos, "resultado") + " " + primero(datos, "unidad");
            case "ESTUDIO_IMAGEN" -> primero(datos, "modalidad") + " - " + primero(datos, "descripcion");
            default -> tipoRegistro;
        };
    }

    private String primero(Map<?, ?> datos, String... campos) {
        for (String campo : campos) {
            String valor = texto(datos.get(campo));
            if (!valor.isBlank()) {
                return valor;
            }
        }
        return "";
    }

    private String texto(Object valor) {
        return valor == null ? "" : String.valueOf(valor);
    }

    private Long numero(Object valor) {
        if (valor instanceof Number numero) {
            return numero.longValue();
        }
        try {
            return valor == null ? null : Long.parseLong(String.valueOf(valor));
        } catch (NumberFormatException ex) {
            return null;
        }
    }

    private String json(Object datos) {
        try {
            return objectMapper.writeValueAsString(datos);
        } catch (JsonProcessingException ex) {
            return "{}";
        }
    }
}
