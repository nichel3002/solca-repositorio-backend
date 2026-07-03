package ec.edu.solca.laboratorio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "resultados_laboratorio")
public class ResultadoLaboratorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String idPacienteRegional;
    private String sede;
    private String fechaResultado;
    private String tipoExamen;
    private String resultado;
    private String unidad;
    private String rangoReferencia;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIdPacienteRegional() { return idPacienteRegional; }
    public void setIdPacienteRegional(String idPacienteRegional) { this.idPacienteRegional = idPacienteRegional; }
    public String getSede() { return sede; }
    public void setSede(String sede) { this.sede = sede; }
    public String getFechaResultado() { return fechaResultado; }
    public void setFechaResultado(String fechaResultado) { this.fechaResultado = fechaResultado; }
    public String getTipoExamen() { return tipoExamen; }
    public void setTipoExamen(String tipoExamen) { this.tipoExamen = tipoExamen; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
    public String getRangoReferencia() { return rangoReferencia; }
    public void setRangoReferencia(String rangoReferencia) { this.rangoReferencia = rangoReferencia; }
}
