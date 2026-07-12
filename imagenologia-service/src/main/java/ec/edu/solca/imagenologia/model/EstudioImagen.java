package ec.edu.solca.imagenologia.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "estudios_imagenologia")
public class EstudioImagen {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
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
    @JsonIgnore
    @Column(columnDefinition = "bytea")
    private byte[] dicomData;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
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
    public byte[] getDicomData() { return dicomData; }
    public void setDicomData(byte[] dicomData) { this.dicomData = dicomData; }
}
