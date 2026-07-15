package ec.edu.solca.repositorio.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "repositorio_imagenologia")
public class RepositorioImagenologia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long idOrigen;
    private String idPacienteRegional;
    private String sede;
    private String fechaEstudio;
    private String modalidad;
    private String descripcion;
    private String urlPacs;
    private String informeRadiologico;
    private String archivoDicom;
    private String protocoloEnvio;
    private String estadoEnvio;
    private Boolean tieneDicom;
    private String actualizadoEn;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getIdOrigen() { return idOrigen; }
    public void setIdOrigen(Long idOrigen) { this.idOrigen = idOrigen; }
    public String getIdPacienteRegional() { return idPacienteRegional; }
    public void setIdPacienteRegional(String idPacienteRegional) { this.idPacienteRegional = idPacienteRegional; }
    public String getSede() { return sede; }
    public void setSede(String sede) { this.sede = sede; }
    public String getFechaEstudio() { return fechaEstudio; }
    public void setFechaEstudio(String fechaEstudio) { this.fechaEstudio = fechaEstudio; }
    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getUrlPacs() { return urlPacs; }
    public void setUrlPacs(String urlPacs) { this.urlPacs = urlPacs; }
    public String getInformeRadiologico() { return informeRadiologico; }
    public void setInformeRadiologico(String informeRadiologico) { this.informeRadiologico = informeRadiologico; }
    public String getArchivoDicom() { return archivoDicom; }
    public void setArchivoDicom(String archivoDicom) { this.archivoDicom = archivoDicom; }
    public String getProtocoloEnvio() { return protocoloEnvio; }
    public void setProtocoloEnvio(String protocoloEnvio) { this.protocoloEnvio = protocoloEnvio; }
    public String getEstadoEnvio() { return estadoEnvio; }
    public void setEstadoEnvio(String estadoEnvio) { this.estadoEnvio = estadoEnvio; }
    public Boolean getTieneDicom() { return tieneDicom; }
    public void setTieneDicom(Boolean tieneDicom) { this.tieneDicom = tieneDicom; }
    public String getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(String actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}
