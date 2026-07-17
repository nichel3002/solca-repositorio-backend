package ec.edu.solca.historia.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "cie10")
public class Cie10 {
    @Id
    @Column(length = 12)
    private String codigo;
    @Column(nullable = false, columnDefinition = "text")
    private String descripcion;
    private String categoria;
    @Column(columnDefinition = "text")
    private String sintomas;
    private String codigoOms;

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public String getSintomas() { return sintomas; }
    public void setSintomas(String sintomas) { this.sintomas = sintomas; }
    public String getCodigoOms() { return codigoOms; }
    public void setCodigoOms(String codigoOms) { this.codigoOms = codigoOms; }
}
