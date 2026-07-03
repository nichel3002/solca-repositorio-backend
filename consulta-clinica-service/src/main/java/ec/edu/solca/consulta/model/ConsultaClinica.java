package ec.edu.solca.consulta.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "consultas_clinicas")
public class ConsultaClinica {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String idPacienteRegional;
    private String sede;
    private String fechaConsulta;
    private String especialidad;
    private String diagnostico;
    private String medicoTratante;
    private String observaciones;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIdPacienteRegional() { return idPacienteRegional; }
    public void setIdPacienteRegional(String idPacienteRegional) { this.idPacienteRegional = idPacienteRegional; }
    public String getSede() { return sede; }
    public void setSede(String sede) { this.sede = sede; }
    public String getFechaConsulta() { return fechaConsulta; }
    public void setFechaConsulta(String fechaConsulta) { this.fechaConsulta = fechaConsulta; }
    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }
    public String getDiagnostico() { return diagnostico; }
    public void setDiagnostico(String diagnostico) { this.diagnostico = diagnostico; }
    public String getMedicoTratante() { return medicoTratante; }
    public void setMedicoTratante(String medicoTratante) { this.medicoTratante = medicoTratante; }
    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }
}
