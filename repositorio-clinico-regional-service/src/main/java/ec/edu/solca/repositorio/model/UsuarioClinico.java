package ec.edu.solca.repositorio.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "usuarios_clinicos")
public class UsuarioClinico {
    @Id
    @Column(length = 120)
    private String username;

    @Column(nullable = false, length = 120)
    private String nombreCompleto;

    @Column(nullable = false, length = 20)
    private String role;

    @Column(nullable = false, length = 80)
    private String sede;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getSede() {
        return sede;
    }

    public void setSede(String sede) {
        this.sede = sede;
    }
}
