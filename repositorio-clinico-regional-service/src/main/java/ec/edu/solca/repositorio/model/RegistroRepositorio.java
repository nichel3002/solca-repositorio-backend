package ec.edu.solca.repositorio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "registro_consultas_repositorio")
public class RegistroRepositorio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idPacienteRegional;
    private String criterioBusqueda;
    private String usuario;
    private String rol;
    private String endpoint;
    private String fechaConsultaRepositorio;
    private String resultado;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIdPacienteRegional() { return idPacienteRegional; }
    public void setIdPacienteRegional(String idPacienteRegional) { this.idPacienteRegional = idPacienteRegional; }
    public String getCriterioBusqueda() { return criterioBusqueda; }
    public void setCriterioBusqueda(String criterioBusqueda) { this.criterioBusqueda = criterioBusqueda; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }
    public String getEndpoint() { return endpoint; }
    public void setEndpoint(String endpoint) { this.endpoint = endpoint; }
    public String getFechaConsultaRepositorio() { return fechaConsultaRepositorio; }
    public void setFechaConsultaRepositorio(String fechaConsultaRepositorio) { this.fechaConsultaRepositorio = fechaConsultaRepositorio; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
}
