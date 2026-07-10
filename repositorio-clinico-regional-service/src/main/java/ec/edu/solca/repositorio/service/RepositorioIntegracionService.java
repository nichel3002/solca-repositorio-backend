package ec.edu.solca.repositorio.service;

import ec.edu.solca.repositorio.dto.HistoriaClinicaRegionalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
public class RepositorioIntegracionService {
    private final RestTemplate restTemplate;
    private final String pacienteUrl;
    private final String consultaUrl;
    private final String laboratorioUrl;
    private final String imagenologiaUrl;

    public RepositorioIntegracionService(
            RestTemplate restTemplate,
            @Value("${servicios.paciente-url}") String pacienteUrl,
            @Value("${servicios.consulta-url}") String consultaUrl,
            @Value("${servicios.laboratorio-url}") String laboratorioUrl,
            @Value("${servicios.imagenologia-url}") String imagenologiaUrl) {
        this.restTemplate = restTemplate;
        this.pacienteUrl = pacienteUrl;
        this.consultaUrl = consultaUrl;
        this.laboratorioUrl = laboratorioUrl;
        this.imagenologiaUrl = imagenologiaUrl;
    }

    public HistoriaClinicaRegionalResponse consolidar(String idPacienteRegional) {
        Map<String, String> errores = new LinkedHashMap<>();
        Object paciente = obtenerObjeto(pacienteUrl + "/pacientes/" + idPacienteRegional, "paciente-maestro", errores);
        List<Object> consultas = obtenerLista(consultaUrl + "/consultas/paciente/" + idPacienteRegional, "consulta-clinica", errores);
        List<Object> laboratorio = obtenerLista(laboratorioUrl + "/laboratorio/paciente/" + idPacienteRegional, "laboratorio-clinico", errores);
        List<Object> imagenes = obtenerLista(imagenologiaUrl + "/imagenes/paciente/" + idPacienteRegional, "imagenologia", errores);
        return new HistoriaClinicaRegionalResponse(paciente, consultas, laboratorio, imagenes, errores);
    }

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
        return new HistoriaClinicaRegionalResponse(paciente, consultas, laboratorio, imagenes, errores);
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
}
