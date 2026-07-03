package ec.edu.solca.repositorio.dto;

import java.util.List;
import java.util.Map;

public class HistoriaClinicaRegionalResponse {
    private Object paciente;
    private List<Object> consultas;
    private List<Object> laboratorio;
    private List<Object> imagenes;
    private Map<String, String> errores;

    public HistoriaClinicaRegionalResponse(Object paciente, List<Object> consultas, List<Object> laboratorio, List<Object> imagenes, Map<String, String> errores) {
        this.paciente = paciente;
        this.consultas = consultas;
        this.laboratorio = laboratorio;
        this.imagenes = imagenes;
        this.errores = errores;
    }

    public Object getPaciente() { return paciente; }
    public void setPaciente(Object paciente) { this.paciente = paciente; }
    public List<Object> getConsultas() { return consultas; }
    public void setConsultas(List<Object> consultas) { this.consultas = consultas; }
    public List<Object> getLaboratorio() { return laboratorio; }
    public void setLaboratorio(List<Object> laboratorio) { this.laboratorio = laboratorio; }
    public List<Object> getImagenes() { return imagenes; }
    public void setImagenes(List<Object> imagenes) { this.imagenes = imagenes; }
    public Map<String, String> getErrores() { return errores; }
    public void setErrores(Map<String, String> errores) { this.errores = errores; }
}
