package ec.edu.solca.repositorio.service;

import ec.edu.solca.repositorio.dto.HistoriaClinicaRegionalResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
}
