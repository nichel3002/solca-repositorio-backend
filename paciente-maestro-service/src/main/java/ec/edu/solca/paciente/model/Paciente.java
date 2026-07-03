package ec.edu.solca.paciente.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "pacientes")
public class Paciente {
    @Id
    @Column(name = "id_paciente_regional", length = 20)
    private String idPacienteRegional;

    @NotBlank
    @Column(nullable = false, unique = true, length = 10)
    private String cedula;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String nombres;

    @NotBlank
    @Column(nullable = false, length = 120)
    private String apellidos;

    @Column(length = 80)
    private String sedeOrigen;

    @Column(length = 40)
    private String fechaNacimiento;

    @Column(length = 20)
    private String sexo;

    @Column(length = 160)
    private String direccion;

    @Column(length = 30)
    private String telefono;

    public String getIdPacienteRegional() {
        return idPacienteRegional;
    }

    public void setIdPacienteRegional(String idPacienteRegional) {
        this.idPacienteRegional = idPacienteRegional;
    }

    public String getCedula() {
        return cedula;
    }

    public void setCedula(String cedula) {
        this.cedula = cedula;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getSedeOrigen() {
        return sedeOrigen;
    }

    public void setSedeOrigen(String sedeOrigen) {
        this.sedeOrigen = sedeOrigen;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
