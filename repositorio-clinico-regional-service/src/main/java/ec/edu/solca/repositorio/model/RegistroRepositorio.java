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
    private String fecha;
    private String hora;
    private String direccionIp;
    private String modulos;
    private String paciente;
    private String accion;
    private String resultado;
    private String resultados;

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
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public String getHora() { return hora; }
    public void setHora(String hora) { this.hora = hora; }
    public String getDireccionIp() { return direccionIp; }
    public void setDireccionIp(String direccionIp) { this.direccionIp = direccionIp; }
    public String getModulos() { return modulos; }
    public void setModulos(String modulos) { this.modulos = modulos; }
    public String getPaciente() { return paciente; }
    public void setPaciente(String paciente) { this.paciente = paciente; }
    public String getAccion() { return accion; }
    public void setAccion(String accion) { this.accion = accion; }
    public String getResultado() { return resultado; }
    public void setResultado(String resultado) { this.resultado = resultado; }
    public String getResultados() { return resultados; }
    public void setResultados(String resultados) { this.resultados = resultados; }
}
