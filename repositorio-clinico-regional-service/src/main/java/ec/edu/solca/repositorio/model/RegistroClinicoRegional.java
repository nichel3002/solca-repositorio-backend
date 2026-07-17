package ec.edu.solca.repositorio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "repositorio_clinico_regional")
public class RegistroClinicoRegional {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String idPacienteRegional;
    private String modulo;
    private String tipoRegistro;
    private String idOrigen;
    private String sede;
    private String fechaRegistro;
    private String cedula;
    private String nombres;
    private String apellidos;
    private String sedeOrigen;
    private String fechaNacimiento;
    private String sexo;
    private String direccion;
    private String telefono;
    private String fechaConsulta;
    private String especialidad;
    private String diagnostico;
    private String medicoTratante;
    private String observaciones;
    private String idHistoriaClinica;
    private String fechaApertura;
    private String codigoCie10;
    private String diagnosticoPrincipal;
    private String motivoConsulta;
    private String enfermedadActual;
    private String estadioClinico;
    private String planTratamiento;
    private String medicoResponsable;
    private String fechaResultado;
    private String tipoExamen;
    private String resultadoLaboratorio;
    private String unidad;
    private String rangoReferencia;
    private String fechaEstudio;
    private String modalidad;
    private String descripcionImagen;
    private String urlPacs;
    private String informeRadiologico;
    private String archivoDicom;
    private String protocoloEnvio;
    private String estadoEnvio;
    private Boolean tieneDicom;
    private String resumen;
    @Column(columnDefinition = "text")
    private String datosJson;
    private String actualizadoEn;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getIdPacienteRegional() { return idPacienteRegional; }
    public void setIdPacienteRegional(String idPacienteRegional) { this.idPacienteRegional = idPacienteRegional; }
    public String getModulo() { return modulo; }
    public void setModulo(String modulo) { this.modulo = modulo; }
    public String getTipoRegistro() { return tipoRegistro; }
    public void setTipoRegistro(String tipoRegistro) { this.tipoRegistro = tipoRegistro; }
    public String getIdOrigen() { return idOrigen; }
    public void setIdOrigen(String idOrigen) { this.idOrigen = idOrigen; }
    public String getSede() { return sede; }
    public void setSede(String sede) { this.sede = sede; }
    public String getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(String fechaRegistro) { this.fechaRegistro = fechaRegistro; }
    public String getCedula() { return cedula; }
    public void setCedula(String cedula) { this.cedula = cedula; }
    public String getNombres() { return nombres; }
    public void setNombres(String nombres) { this.nombres = nombres; }
    public String getApellidos() { return apellidos; }
    public void setApellidos(String apellidos) { this.apellidos = apellidos; }
    public String getSedeOrigen() { return sedeOrigen; }
    public void setSedeOrigen(String sedeOrigen) { this.sedeOrigen = sedeOrigen; }
    public String getFechaNacimiento() { return fechaNacimiento; }
    public void setFechaNacimiento(String fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
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
    public String getIdHistoriaClinica() { return idHistoriaClinica; }
    public void setIdHistoriaClinica(String idHistoriaClinica) { this.idHistoriaClinica = idHistoriaClinica; }
    public String getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(String fechaApertura) { this.fechaApertura = fechaApertura; }
    public String getCodigoCie10() { return codigoCie10; }
    public void setCodigoCie10(String codigoCie10) { this.codigoCie10 = codigoCie10; }
    public String getDiagnosticoPrincipal() { return diagnosticoPrincipal; }
    public void setDiagnosticoPrincipal(String diagnosticoPrincipal) { this.diagnosticoPrincipal = diagnosticoPrincipal; }
    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }
    public String getEnfermedadActual() { return enfermedadActual; }
    public void setEnfermedadActual(String enfermedadActual) { this.enfermedadActual = enfermedadActual; }
    public String getEstadioClinico() { return estadioClinico; }
    public void setEstadioClinico(String estadioClinico) { this.estadioClinico = estadioClinico; }
    public String getPlanTratamiento() { return planTratamiento; }
    public void setPlanTratamiento(String planTratamiento) { this.planTratamiento = planTratamiento; }
    public String getMedicoResponsable() { return medicoResponsable; }
    public void setMedicoResponsable(String medicoResponsable) { this.medicoResponsable = medicoResponsable; }
    public String getFechaResultado() { return fechaResultado; }
    public void setFechaResultado(String fechaResultado) { this.fechaResultado = fechaResultado; }
    public String getTipoExamen() { return tipoExamen; }
    public void setTipoExamen(String tipoExamen) { this.tipoExamen = tipoExamen; }
    public String getResultadoLaboratorio() { return resultadoLaboratorio; }
    public void setResultadoLaboratorio(String resultadoLaboratorio) { this.resultadoLaboratorio = resultadoLaboratorio; }
    public String getUnidad() { return unidad; }
    public void setUnidad(String unidad) { this.unidad = unidad; }
    public String getRangoReferencia() { return rangoReferencia; }
    public void setRangoReferencia(String rangoReferencia) { this.rangoReferencia = rangoReferencia; }
    public String getFechaEstudio() { return fechaEstudio; }
    public void setFechaEstudio(String fechaEstudio) { this.fechaEstudio = fechaEstudio; }
    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }
    public String getDescripcionImagen() { return descripcionImagen; }
    public void setDescripcionImagen(String descripcionImagen) { this.descripcionImagen = descripcionImagen; }
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
    public String getResumen() { return resumen; }
    public void setResumen(String resumen) { this.resumen = resumen; }
    public String getDatosJson() { return datosJson; }
    public void setDatosJson(String datosJson) { this.datosJson = datosJson; }
    public String getActualizadoEn() { return actualizadoEn; }
    public void setActualizadoEn(String actualizadoEn) { this.actualizadoEn = actualizadoEn; }
}
