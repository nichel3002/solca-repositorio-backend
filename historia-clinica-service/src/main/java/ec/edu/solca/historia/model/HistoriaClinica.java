package ec.edu.solca.historia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "historias_clinicas")
public class HistoriaClinica {
    @Id
    @Column(length = 32)
    private String idHistoriaClinica;

    @NotBlank
    @Column(nullable = false, length = 20)
    private String idPacienteRegional;

    private String fechaApertura;
    private String sedeApertura;
    private String estadoHistoria;

    private String estadoCivil;
    private String ocupacion;
    private String instruccion;
    private String nacionalidad;
    private String contactoEmergencia;
    private String telefonoEmergencia;

    @Column(columnDefinition = "text")
    private String antecedentesPatologicosPersonales;
    @Column(columnDefinition = "text")
    private String antecedentesQuirurgicos;
    @Column(columnDefinition = "text")
    private String antecedentesAlergicos;
    @Column(columnDefinition = "text")
    private String antecedentesGinecoObstetricos;
    @Column(columnDefinition = "text")
    private String antecedentesToxicos;
    @Column(columnDefinition = "text")
    private String medicacionHabitual;
    @Column(columnDefinition = "text")
    private String antecedentesOncologicosFamiliares;
    @Column(columnDefinition = "text")
    private String parentescoAntecedenteOncologico;
    @Column(columnDefinition = "text")
    private String otrasEnfermedadesFamiliares;

    @Column(columnDefinition = "text")
    private String motivoConsulta;
    @Column(columnDefinition = "text")
    private String enfermedadActual;
    private String fechaInicioSintomas;
    @Column(columnDefinition = "text")
    private String signosSintomasPrincipales;
    @Column(columnDefinition = "text")
    private String tratamientosPrevios;

    @Column(length = 12)
    private String codigoCie10;
    private String diagnosticoPrincipal;
    @Column(columnDefinition = "text")
    private String diagnosticosSecundarios;
    private String tipoCancer;
    private String localizacionTumor;
    private String lateralidad;
    private String estadioClinico;
    private String clasificacionTnm;
    private String fechaDiagnostico;
    private String baseDiagnostica;
    private String histologia;
    private String gradoTumoral;
    @Column(columnDefinition = "text")
    private String biomarcadores;

    private String peso;
    private String talla;
    private String imc;
    private String presionArterial;
    private String frecuenciaCardiaca;
    private String frecuenciaRespiratoria;
    private String temperatura;
    private String saturacionOxigeno;
    private String estadoFuncionalEcog;
    @Column(columnDefinition = "text")
    private String examenFisicoGeneral;
    @Column(columnDefinition = "text")
    private String examenFisicoRegional;

    private String intencionTratamiento;
    @Column(columnDefinition = "text")
    private String planTratamiento;
    private String cirugiaProgramada;
    private String quimioterapia;
    private String radioterapia;
    private String inmunoterapia;
    private String hormonoterapia;
    private String cuidadosPaliativos;
    private String fechaInicioTratamiento;
    private String medicoResponsable;

    @Column(columnDefinition = "text")
    private String evolucionClinica;
    private String respuestaTratamiento;
    @Column(columnDefinition = "text")
    private String complicaciones;
    private String proximaCita;
    @Column(columnDefinition = "text")
    private String recomendaciones;
    private String estadoActualPaciente;

    private String consentimientoInformado;
    private String fechaConsentimiento;
    private String responsableLegal;
    @Column(columnDefinition = "text")
    private String observacionesLegales;

    private String creadoPor;
    private String rolCreador;
    private String fechaCreacion;
    private String horaCreacion;
    private String actualizadoPor;
    private String fechaActualizacion;
    private String sedeRegistro;

    @PrePersist
    void prePersist() {
        LocalDateTime ahora = LocalDateTime.now();
        if (idHistoriaClinica == null || idHistoriaClinica.isBlank()) {
            idHistoriaClinica = "HC-" + ahora.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        }
        if (fechaApertura == null || fechaApertura.isBlank()) {
            fechaApertura = LocalDate.now().toString();
        }
        if (estadoHistoria == null || estadoHistoria.isBlank()) {
            estadoHistoria = "ACTIVA";
        }
        if (fechaCreacion == null || fechaCreacion.isBlank()) {
            fechaCreacion = LocalDate.now().toString();
        }
        if (horaCreacion == null || horaCreacion.isBlank()) {
            horaCreacion = ahora.toLocalTime().withNano(0).toString();
        }
        fechaActualizacion = ahora.toString();
    }

    public String getIdHistoriaClinica() { return idHistoriaClinica; }
    public void setIdHistoriaClinica(String idHistoriaClinica) { this.idHistoriaClinica = idHistoriaClinica; }
    public String getIdPacienteRegional() { return idPacienteRegional; }
    public void setIdPacienteRegional(String idPacienteRegional) { this.idPacienteRegional = idPacienteRegional; }
    public String getFechaApertura() { return fechaApertura; }
    public void setFechaApertura(String fechaApertura) { this.fechaApertura = fechaApertura; }
    public String getSedeApertura() { return sedeApertura; }
    public void setSedeApertura(String sedeApertura) { this.sedeApertura = sedeApertura; }
    public String getEstadoHistoria() { return estadoHistoria; }
    public void setEstadoHistoria(String estadoHistoria) { this.estadoHistoria = estadoHistoria; }
    public String getEstadoCivil() { return estadoCivil; }
    public void setEstadoCivil(String estadoCivil) { this.estadoCivil = estadoCivil; }
    public String getOcupacion() { return ocupacion; }
    public void setOcupacion(String ocupacion) { this.ocupacion = ocupacion; }
    public String getInstruccion() { return instruccion; }
    public void setInstruccion(String instruccion) { this.instruccion = instruccion; }
    public String getNacionalidad() { return nacionalidad; }
    public void setNacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; }
    public String getContactoEmergencia() { return contactoEmergencia; }
    public void setContactoEmergencia(String contactoEmergencia) { this.contactoEmergencia = contactoEmergencia; }
    public String getTelefonoEmergencia() { return telefonoEmergencia; }
    public void setTelefonoEmergencia(String telefonoEmergencia) { this.telefonoEmergencia = telefonoEmergencia; }
    public String getAntecedentesPatologicosPersonales() { return antecedentesPatologicosPersonales; }
    public void setAntecedentesPatologicosPersonales(String antecedentesPatologicosPersonales) { this.antecedentesPatologicosPersonales = antecedentesPatologicosPersonales; }
    public String getAntecedentesQuirurgicos() { return antecedentesQuirurgicos; }
    public void setAntecedentesQuirurgicos(String antecedentesQuirurgicos) { this.antecedentesQuirurgicos = antecedentesQuirurgicos; }
    public String getAntecedentesAlergicos() { return antecedentesAlergicos; }
    public void setAntecedentesAlergicos(String antecedentesAlergicos) { this.antecedentesAlergicos = antecedentesAlergicos; }
    public String getAntecedentesGinecoObstetricos() { return antecedentesGinecoObstetricos; }
    public void setAntecedentesGinecoObstetricos(String antecedentesGinecoObstetricos) { this.antecedentesGinecoObstetricos = antecedentesGinecoObstetricos; }
    public String getAntecedentesToxicos() { return antecedentesToxicos; }
    public void setAntecedentesToxicos(String antecedentesToxicos) { this.antecedentesToxicos = antecedentesToxicos; }
    public String getMedicacionHabitual() { return medicacionHabitual; }
    public void setMedicacionHabitual(String medicacionHabitual) { this.medicacionHabitual = medicacionHabitual; }
    public String getAntecedentesOncologicosFamiliares() { return antecedentesOncologicosFamiliares; }
    public void setAntecedentesOncologicosFamiliares(String antecedentesOncologicosFamiliares) { this.antecedentesOncologicosFamiliares = antecedentesOncologicosFamiliares; }
    public String getParentescoAntecedenteOncologico() { return parentescoAntecedenteOncologico; }
    public void setParentescoAntecedenteOncologico(String parentescoAntecedenteOncologico) { this.parentescoAntecedenteOncologico = parentescoAntecedenteOncologico; }
    public String getOtrasEnfermedadesFamiliares() { return otrasEnfermedadesFamiliares; }
    public void setOtrasEnfermedadesFamiliares(String otrasEnfermedadesFamiliares) { this.otrasEnfermedadesFamiliares = otrasEnfermedadesFamiliares; }
    public String getMotivoConsulta() { return motivoConsulta; }
    public void setMotivoConsulta(String motivoConsulta) { this.motivoConsulta = motivoConsulta; }
    public String getEnfermedadActual() { return enfermedadActual; }
    public void setEnfermedadActual(String enfermedadActual) { this.enfermedadActual = enfermedadActual; }
    public String getFechaInicioSintomas() { return fechaInicioSintomas; }
    public void setFechaInicioSintomas(String fechaInicioSintomas) { this.fechaInicioSintomas = fechaInicioSintomas; }
    public String getSignosSintomasPrincipales() { return signosSintomasPrincipales; }
    public void setSignosSintomasPrincipales(String signosSintomasPrincipales) { this.signosSintomasPrincipales = signosSintomasPrincipales; }
    public String getTratamientosPrevios() { return tratamientosPrevios; }
    public void setTratamientosPrevios(String tratamientosPrevios) { this.tratamientosPrevios = tratamientosPrevios; }
    public String getCodigoCie10() { return codigoCie10; }
    public void setCodigoCie10(String codigoCie10) { this.codigoCie10 = codigoCie10; }
    public String getDiagnosticoPrincipal() { return diagnosticoPrincipal; }
    public void setDiagnosticoPrincipal(String diagnosticoPrincipal) { this.diagnosticoPrincipal = diagnosticoPrincipal; }
    public String getDiagnosticosSecundarios() { return diagnosticosSecundarios; }
    public void setDiagnosticosSecundarios(String diagnosticosSecundarios) { this.diagnosticosSecundarios = diagnosticosSecundarios; }
    public String getTipoCancer() { return tipoCancer; }
    public void setTipoCancer(String tipoCancer) { this.tipoCancer = tipoCancer; }
    public String getLocalizacionTumor() { return localizacionTumor; }
    public void setLocalizacionTumor(String localizacionTumor) { this.localizacionTumor = localizacionTumor; }
    public String getLateralidad() { return lateralidad; }
    public void setLateralidad(String lateralidad) { this.lateralidad = lateralidad; }
    public String getEstadioClinico() { return estadioClinico; }
    public void setEstadioClinico(String estadioClinico) { this.estadioClinico = estadioClinico; }
    public String getClasificacionTnm() { return clasificacionTnm; }
    public void setClasificacionTnm(String clasificacionTnm) { this.clasificacionTnm = clasificacionTnm; }
    public String getFechaDiagnostico() { return fechaDiagnostico; }
    public void setFechaDiagnostico(String fechaDiagnostico) { this.fechaDiagnostico = fechaDiagnostico; }
    public String getBaseDiagnostica() { return baseDiagnostica; }
    public void setBaseDiagnostica(String baseDiagnostica) { this.baseDiagnostica = baseDiagnostica; }
    public String getHistologia() { return histologia; }
    public void setHistologia(String histologia) { this.histologia = histologia; }
    public String getGradoTumoral() { return gradoTumoral; }
    public void setGradoTumoral(String gradoTumoral) { this.gradoTumoral = gradoTumoral; }
    public String getBiomarcadores() { return biomarcadores; }
    public void setBiomarcadores(String biomarcadores) { this.biomarcadores = biomarcadores; }
    public String getPeso() { return peso; }
    public void setPeso(String peso) { this.peso = peso; }
    public String getTalla() { return talla; }
    public void setTalla(String talla) { this.talla = talla; }
    public String getImc() { return imc; }
    public void setImc(String imc) { this.imc = imc; }
    public String getPresionArterial() { return presionArterial; }
    public void setPresionArterial(String presionArterial) { this.presionArterial = presionArterial; }
    public String getFrecuenciaCardiaca() { return frecuenciaCardiaca; }
    public void setFrecuenciaCardiaca(String frecuenciaCardiaca) { this.frecuenciaCardiaca = frecuenciaCardiaca; }
    public String getFrecuenciaRespiratoria() { return frecuenciaRespiratoria; }
    public void setFrecuenciaRespiratoria(String frecuenciaRespiratoria) { this.frecuenciaRespiratoria = frecuenciaRespiratoria; }
    public String getTemperatura() { return temperatura; }
    public void setTemperatura(String temperatura) { this.temperatura = temperatura; }
    public String getSaturacionOxigeno() { return saturacionOxigeno; }
    public void setSaturacionOxigeno(String saturacionOxigeno) { this.saturacionOxigeno = saturacionOxigeno; }
    public String getEstadoFuncionalEcog() { return estadoFuncionalEcog; }
    public void setEstadoFuncionalEcog(String estadoFuncionalEcog) { this.estadoFuncionalEcog = estadoFuncionalEcog; }
    public String getExamenFisicoGeneral() { return examenFisicoGeneral; }
    public void setExamenFisicoGeneral(String examenFisicoGeneral) { this.examenFisicoGeneral = examenFisicoGeneral; }
    public String getExamenFisicoRegional() { return examenFisicoRegional; }
    public void setExamenFisicoRegional(String examenFisicoRegional) { this.examenFisicoRegional = examenFisicoRegional; }
    public String getIntencionTratamiento() { return intencionTratamiento; }
    public void setIntencionTratamiento(String intencionTratamiento) { this.intencionTratamiento = intencionTratamiento; }
    public String getPlanTratamiento() { return planTratamiento; }
    public void setPlanTratamiento(String planTratamiento) { this.planTratamiento = planTratamiento; }
    public String getCirugiaProgramada() { return cirugiaProgramada; }
    public void setCirugiaProgramada(String cirugiaProgramada) { this.cirugiaProgramada = cirugiaProgramada; }
    public String getQuimioterapia() { return quimioterapia; }
    public void setQuimioterapia(String quimioterapia) { this.quimioterapia = quimioterapia; }
    public String getRadioterapia() { return radioterapia; }
    public void setRadioterapia(String radioterapia) { this.radioterapia = radioterapia; }
    public String getInmunoterapia() { return inmunoterapia; }
    public void setInmunoterapia(String inmunoterapia) { this.inmunoterapia = inmunoterapia; }
    public String getHormonoterapia() { return hormonoterapia; }
    public void setHormonoterapia(String hormonoterapia) { this.hormonoterapia = hormonoterapia; }
    public String getCuidadosPaliativos() { return cuidadosPaliativos; }
    public void setCuidadosPaliativos(String cuidadosPaliativos) { this.cuidadosPaliativos = cuidadosPaliativos; }
    public String getFechaInicioTratamiento() { return fechaInicioTratamiento; }
    public void setFechaInicioTratamiento(String fechaInicioTratamiento) { this.fechaInicioTratamiento = fechaInicioTratamiento; }
    public String getMedicoResponsable() { return medicoResponsable; }
    public void setMedicoResponsable(String medicoResponsable) { this.medicoResponsable = medicoResponsable; }
    public String getEvolucionClinica() { return evolucionClinica; }
    public void setEvolucionClinica(String evolucionClinica) { this.evolucionClinica = evolucionClinica; }
    public String getRespuestaTratamiento() { return respuestaTratamiento; }
    public void setRespuestaTratamiento(String respuestaTratamiento) { this.respuestaTratamiento = respuestaTratamiento; }
    public String getComplicaciones() { return complicaciones; }
    public void setComplicaciones(String complicaciones) { this.complicaciones = complicaciones; }
    public String getProximaCita() { return proximaCita; }
    public void setProximaCita(String proximaCita) { this.proximaCita = proximaCita; }
    public String getRecomendaciones() { return recomendaciones; }
    public void setRecomendaciones(String recomendaciones) { this.recomendaciones = recomendaciones; }
    public String getEstadoActualPaciente() { return estadoActualPaciente; }
    public void setEstadoActualPaciente(String estadoActualPaciente) { this.estadoActualPaciente = estadoActualPaciente; }
    public String getConsentimientoInformado() { return consentimientoInformado; }
    public void setConsentimientoInformado(String consentimientoInformado) { this.consentimientoInformado = consentimientoInformado; }
    public String getFechaConsentimiento() { return fechaConsentimiento; }
    public void setFechaConsentimiento(String fechaConsentimiento) { this.fechaConsentimiento = fechaConsentimiento; }
    public String getResponsableLegal() { return responsableLegal; }
    public void setResponsableLegal(String responsableLegal) { this.responsableLegal = responsableLegal; }
    public String getObservacionesLegales() { return observacionesLegales; }
    public void setObservacionesLegales(String observacionesLegales) { this.observacionesLegales = observacionesLegales; }
    public String getCreadoPor() { return creadoPor; }
    public void setCreadoPor(String creadoPor) { this.creadoPor = creadoPor; }
    public String getRolCreador() { return rolCreador; }
    public void setRolCreador(String rolCreador) { this.rolCreador = rolCreador; }
    public String getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(String fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    public String getHoraCreacion() { return horaCreacion; }
    public void setHoraCreacion(String horaCreacion) { this.horaCreacion = horaCreacion; }
    public String getActualizadoPor() { return actualizadoPor; }
    public void setActualizadoPor(String actualizadoPor) { this.actualizadoPor = actualizadoPor; }
    public String getFechaActualizacion() { return fechaActualizacion; }
    public void setFechaActualizacion(String fechaActualizacion) { this.fechaActualizacion = fechaActualizacion; }
    public String getSedeRegistro() { return sedeRegistro; }
    public void setSedeRegistro(String sedeRegistro) { this.sedeRegistro = sedeRegistro; }
}
